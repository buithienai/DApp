pragma solidity ^0.5.0;

contract PatientProfile {
    bytes32 public _patientData;

    address public _creator;

    constructor(address creator, bytes32 patienData) public
    {
        _creator = creator;
        _patientData = patienData;
    }


    function getPatientProfile() public view returns (bytes32) {
        return _patientData;
    }

    function getCreator() public view returns(address) {
        return _creator;
    }

}
