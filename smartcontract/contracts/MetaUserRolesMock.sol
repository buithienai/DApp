pragma solidity ^0.5.0;

import "./MetaUserRoles.sol";

contract MetaUserRolesMock  is MetaUserRoles{
    uint public version = 20;

    function getVersion()
    public
    view
    returns(uint) {
        return version;
    }

    function getSuperAdminMock()
    public
    view
    returns(address)
    {
        return super.getSuperAdmin();
    }
}
