pragma solidity ^0.5.0;

import "./Roles.sol";
import "./IUserRoles.sol";

contract MetaUserRoles is IUserRoles{
    using Roles for Roles.Role;


    /**
    *@storage
    */
    address private _superAdmin;

    address private _operationAdmin;


    bool public _initialized = false;

    address public _relay;


    /**
    * 1 - hospital director
    * 2 - doctor
    * 3 -  assistant
    */
    mapping(uint8 => Roles.Role) _accountRoles;



    /**
    *@dev even - for add pending list and odd - for removing pending list
    * 1 - list of pending account for add hospital director
    * 2 - list of pending account for remove hospital director
    * 3 - list of pending account for add doctor
    * 4 - list of pending account for remove doctor
    * 5 - list of pending account for add assistant
    * 6- list of pending account for remove assistant
    */
    mapping(uint8 => mapping(address => bool)) private _pendingAccount;



    /**
    *@ event
    */
    event UpdateSuperAdmin(address indexed exSuperAdmin, address newSuperAdmin);
    event UpdateOperationAdmin(address indexed exOperationAdmin, address newOperationAdmin);


    event PendingAccount(uint indexed index, address indexed account);
    event RevokePendingAccount(uint indexed index, address indexed account);

    event AddIndexAccount(uint index, address account);
    event RemoveIndexAccount(uint index, address account);


    function initialize(address superAdmin, address operationAdmin, address relay) public {
        require(! _initialized);
        require(superAdmin != address(0) && superAdmin != operationAdmin);
        _superAdmin = superAdmin;
        _operationAdmin = operationAdmin;
        _relay = relay;
        _initialized = true;

    }

    modifier onlyAuthorized() {
        require(msg.sender == _relay || checkMessageData(msg.sender));
        _;
    }


    //Checks that address a is the first input in msg.data.
    //Has very minimal gas overhead.
    function checkMessageData(address a) internal view returns (bool t) {
        if (msg.data.length < 36) return false;
        assembly {
            let mask := 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
            t := eq(a, and(mask, calldataload(4)))
        }
    }


    /*
    * @dev Gets an address encoded as the first argument in transaction data
    * @param b The byte array that should have an address as first argument
    * @returns a The address retrieved from the array
    (Optimization based on work by tjade273)
    */
    function getAddress(bytes memory b) public pure returns (address a) {
        if (b.length < 36) return address(0);
        assembly {
            let mask := 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
            a := and(mask, mload(add(b, 36)))
        // 36 is the offset of the first parameter of the data, if encoded properly.
        // 32 bytes for the length of the bytes array, and 4 bytes for the function signature.
        }
    }


    //event Testing(address sender, address owner);
    /**
    *@modifier
    */

    modifier onlySuperAdmin() {
       // emit Testing(msg.sender, _relay);
        require(msg.sender == _relay || checkMessageData(msg.sender));
        address claimedSender = getAddress(msg.data);
        require(isSuperAdmin(claimedSender),"Super Admin Role: caller does not have the super admin role");
        _;
    }



    modifier onlyOperationAdmin() {
        require(msg.sender == _relay || checkMessageData(msg.sender));
        address claimedSender = getAddress(msg.data);
        require(isOperationAdmin(claimedSender),"Operation Admin Roles: caller does not have the operation admin role");
        _;
    }


    modifier onlyAuthorizationUser(){
        require(msg.sender == _relay || checkMessageData(msg.sender));
        address claimedSender = getAddress(msg.data);
        require(isOperationAdmin(claimedSender) || isSuperAdmin(claimedSender),"caller does not have the operation admin role or super admin role");
        _;
    }

    modifier notNull(address account) {
        require(account != address(0), "account should not null address");
        _;
    }




    function isSuperAdmin(address account)
    public
    view
    returns(bool)
    {
        return (account == _superAdmin);
    }

    function getSuperAdmin() public view returns (address) {
        return _superAdmin;
    }

    function updateSuperAdmin(address sender, address newSuperAdmin)
    public
    onlySuperAdmin
    notNull(newSuperAdmin)
    {
        _superAdmin = newSuperAdmin;
        emit UpdateSuperAdmin(sender, newSuperAdmin);
    }


    function isOperationAdmin(address account)
    public
    view
    returns(bool)
    {
        return( account == _operationAdmin);
    }


    function getOperationAdmin() public view returns (address) {
        return _operationAdmin;
    }


    function updateOperationAdmin(address sender, address newOperationAdmin)
    public
    onlyOperationAdmin
    notNull(newOperationAdmin)
    {
        _operationAdmin = newOperationAdmin;
        emit UpdateOperationAdmin(msg.sender, newOperationAdmin);
    }


    function isPendingAccount(uint8 index, address account) public view returns (bool) {
        return _pendingAccount[index][account];
    }



    function _addPendingAccount(uint8 index, address account) internal {
        _pendingAccount[index][account] = true;
        emit PendingAccount(index, account);
    }

    function _removePendingAccount(uint8 index, address account) internal {
        require(isPendingAccount(index, account), "account is not the pending list");
        _pendingAccount[index][account] = false;
        emit RevokePendingAccount(index, account);
    }






    /**
    *@dev   add the account into the pending list for approving by Opeartion Admin
    */
    function submitAddAccount(address sender, uint8 index, address[] memory accounts)
    public
    onlyOperationAdmin
    {
        for(uint i = 0 ; i < accounts.length ; i++) {
            require(! isValidIndexAccount(index, accounts[i]), " account is the valid index account");
            _addPendingAccount(2*index -1, accounts[i]);
        }

    }


    /**
    *@dev approve an account by only by SuperAdmin
    */
    function approveAddAccount(address sender, uint8 index, address[] memory accounts)
    public
    onlySuperAdmin
    {
        for (uint i = 0; i < accounts.length; i++) {
            require(! isValidIndexAccount(index,accounts[i])," account is already the valid index account");
            _removePendingAccount(2*index - 1,accounts[i]);
            _addIndexAccount(index, accounts[i]);
        }

    }



    function _addIndexAccount(uint8 index, address account) internal {
        _accountRoles[index].add(account);
        emit AddIndexAccount(index, account);
    }




    function isValidIndexAccount(uint8 index, address account) public view returns (bool) {
        return _accountRoles[index].has(account);
    }




    /**
   *@dev only operation admin is able to submit the Remove Index Account list
   */
    function submitRemoveIndexAccount(address sender, uint8 index, address[] memory accounts)
    public
    onlyOperationAdmin
    {
        for(uint i = 0; i < accounts.length; i++) {
            require( isValidIndexAccount(index, accounts[i]), "account is not a valid index account");
            _addPendingAccount(2*index, accounts[i]);
        }
    }


    /**
    *@dev only Super Admin is able to approve to remove the pending auditor account
    */
    function approveRemoveIndexAccount(address sender, uint8 index, address[] memory accounts)
    public
    onlySuperAdmin
    {
        for(uint i = 0; i < accounts.length; i++) {
            _removePendingAccount(2*index, accounts[i]);
            _removeIndexAccount(index, accounts[i]);
        }
    }


    function _removeIndexAccount(uint8 index, address account)
    internal
    {
        _accountRoles[index].remove(account);
        emit RemoveIndexAccount(index, account);
    }



    /**
   *@dev Remove account from pending list and only available for operation admin
   */
    function revokePendingAddAccount(address sender, uint8 index, address[] memory accounts)
    public
    onlyAuthorizationUser
    {
        for(uint i = 0 ; i < accounts.length; i++) {
            _removePendingAccount(2*index -1, accounts[i]);
        }

    }

    /**
   *@dev Remove account from pending list and only available for operation admin
   */
    function revokePendingRemoveAccount(address sender, uint8 index, address[] memory accounts)
    public
    onlyAuthorizationUser
    {
        for(uint i = 0 ; i < accounts.length; i++) {
            _removePendingAccount(2*index, accounts[i]);
        }

    }





}
