pragma solidity ^0.5.0;


interface IUserRoles {

    function isSuperAdmin(address account) external view returns(bool);

    function isOperationAdmin(address account) external view returns(bool);

    function isValidIndexAccount(uint8 index, address account) external view returns (bool);



}
