pragma solidity ^0.5.0;

import "./Factory.sol";
import "./IUserRoles.sol";
import "./PatientProfile.sol";

contract MetaKLNetwork is Factory{

    /**
    *@Storage
    */
    IUserRoles public _userRoles;
    mapping(uint => address) public registeredProfile;

    uint256 public _level = 6;

    bool public _initialized = false;

    address public _relay;

    mapping(uint => uint) public _voteNumber;
    uint public _profileCount;

    mapping(uint => patientProfile) _pendingPatientProfile;

    struct patientProfile{
        bytes32 patientData;
        bool executed;
    }


    function initialize(IUserRoles userRoles, uint256 level, address relay) public {
        require(! _initialized);
        _userRoles = userRoles;
        _level = level;
        _relay = relay;
        _initialized = true;

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


    //Checks that address a is the first input in msg.data.
    //Has very minimal gas overhead.
    function checkMessageData(address a) internal view returns (bool t) {
        if (msg.data.length < 36) return false;
        assembly {
            let mask := 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
            t := eq(a, and(mask, calldataload(4)))
        }
    }



    function createVoteProfile(address sender, uint8 indexRole, bytes32 _patientData)
    public
    returns (uint voteProfile)
    {
        require(_userRoles.isValidIndexAccount(indexRole, sender));
        _profileCount += 1;
        if(indexRole == 1) {
            _voteNumber[_profileCount] = 5;
        } else if(indexRole == 2) {
            _voteNumber[_profileCount] = 3;
        } else {
            _voteNumber[_profileCount] = 2;
        }



        _pendingPatientProfile[_profileCount] = patientProfile({
               patientData:_patientData,
               excuted:false
            });
        _execute(_profileCount);

    }


    function _execute(uint _profileId)
    internal
    {

        if(_voteNumber[_profileId] >= _level) {
            registeredProfile[_profileId] = _createPatientProfile(_profileId);

        }
    }


    /**
    *@dev create patien profile
    */
    function _createPatientProfile(uint _profileId)
    internal
    returns( address)
    {
        patientProfile storage _patient = _patientProfile[_profileId];

        PatientProfile  createdPatientProfile = new PatientProfile(address(this), _patient.patientData);
        register(sender, address(createdPatientProfile));
        return address(createdPatientProfile);
    }


    function getPatientProfileById(uint _profileId)
    external
    view
    returns(address)
    {
        return registeredProfile[_profileId];
    }



}
