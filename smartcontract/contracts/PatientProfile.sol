pragma solidity ^0.5.0;

contract PatientProfile {
    bytes32 public _patientData;

    address public _creator;

    constructor(address creator, bytes32 patienData) public
    {
        _creator = creator;
        _patientData = patienData;
    }

}
