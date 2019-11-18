pragma solidity ^0.5.0;


// This contract is meant as a "singleton" forwarding contract.
// Eventually, it will be able to forward any transaction to
// Any contract that is built to accept it.
contract TxRelay {

    // Note: This is a local nonce.
    // Different from the nonce defined w/in protocol.
    mapping(address => uint) nonce;

    // This mapping specifies a whitelist of allowed senders for transactions.
    // There can be one whitelist per ethereum account, which is the owner of that
    // whitelist. Users can specify which whitelist they want to use when signing
    // a transaction. They can use their own whitelist, a whitelist belonging
    // to another account, or skip using a whitelist by specifying the zero address.
    mapping(address => mapping(address => bool)) public whitelist;


    mapping(uint8 => string) public nibbleDictionary;


    event Sender(address _sender);



    constructor() public {
        ////create nibble Dictionary
        nibbleDictionary[0]='0';
        nibbleDictionary[1]='1';
        nibbleDictionary[2]='2';
        nibbleDictionary[3]='3';
        nibbleDictionary[4]='4';
        nibbleDictionary[5]='5';
        nibbleDictionary[6]='6';
        nibbleDictionary[7]='7';
        nibbleDictionary[8]='8';
        nibbleDictionary[9]='9';
        nibbleDictionary[10]='a';
        nibbleDictionary[11]='b';
        nibbleDictionary[12]='c';
        nibbleDictionary[13]='d';
        nibbleDictionary[14]='e';
        nibbleDictionary[15]='f';

    }



    /*
     * @dev Relays meta transactions
     * @param sigV, sigR, sigS ECDSA signature on some data to be forwarded
     * @param destination Location the meta-tx should be forwarded to
     * @param data The bytes necessary to call the function in the destination contract.
     * Note: The first encoded argument in data must be address of the signer. This means
     * that all functions called from this relay must take an address as the first parameter.
     */
    function relayMetaTx(
        uint8 sigV,
        bytes32 sigR,
        bytes32 sigS,
        address destination,
        bytes memory data,
        address listOwner,
        bool isHardWallet
    ) public {

        // only allow senders from the whitelist specified by the user,
        // 0x0 means no whitelist.
        require(listOwner == address(0) || whitelist[listOwner][msg.sender]);

        address claimedSender = getAddress(data);
        bytes32 hashMessage;

        if(isHardWallet) {
            bytes memory prefix = "\x19Ethereum Signed Message:\n64";
            bytes32 h = keccak256(abi.encodePacked(byte(0x19), byte(0), this, listOwner, nonce[claimedSender], destination, data));
            bytes memory hashBytes = bytes32ToString(h);
            hashMessage = keccak256(abi.encodePacked(prefix,hashBytes));
        } else {

            // use EIP 191
            // 0x19 :: version :: relay :: whitelistOwner :: nonce :: destination :: data
            hashMessage = keccak256(abi.encodePacked(byte(0x19), byte(0), this, listOwner, nonce[claimedSender], destination, data));

        }

        address addressFromSig = ecrecover(hashMessage, sigV, sigR, sigS);

        emit Sender(addressFromSig);
        require(claimedSender == addressFromSig);

        nonce[claimedSender]++; //if we are going to do tx, update nonce

        (bool success,) = destination.call(data);

        require(success);
    }



    function bytes32ToString(bytes32 _bytes32) public payable returns (bytes memory) {

        bytes memory stringBytes;
        uint8 b;
        uint8 leftNibble;
        uint8 rightNibble;
        uint8 maskLeft = 15 << 4;

        for (uint256 i; i < 32; i++) {
            b = uint8(_bytes32[i]);
            rightNibble = b % 2 ** 4;
            leftNibble = (b & maskLeft) >> 4;
            stringBytes = abi.encodePacked(stringBytes,nibbleDictionary[leftNibble],nibbleDictionary[rightNibble]);
        }
        return stringBytes;
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

    /*
     * @dev Returns the local nonce of an account.
     * @param add The address to return the nonce for.
     * @return The specific-to-this-contract nonce of the address provided
     */
    function getNonce(address add) public view returns (uint) {
        return nonce[add];
    }

    /*
     * @dev Adds a number of addresses to a specific whitelist. Only
     * the owner of a whitelist can add to it.
     * @param sendersToUpdate the addresses to add to the whitelist
     */
    function addToWhitelist(address[] memory sendersToUpdate) public {
        updateWhitelist(sendersToUpdate, true);
    }

    /*
     * @dev Removes a number of addresses from a specific whitelist. Only
     * the owner of a whitelist can remove from it.
     * @param sendersToUpdate the addresses to add to the whitelist
     */
    function removeFromWhitelist(address[] memory sendersToUpdate) public {
        updateWhitelist(sendersToUpdate, false);
    }

    /*
     * @dev Internal logic to update a whitelist
     * @param sendersToUpdate the addresses to add to the whitelist
     * @param newStatus whether to add or remove addresses
     */
    function updateWhitelist(address[] memory sendersToUpdate, bool newStatus) private {
        for (uint i = 0; i < sendersToUpdate.length; i++) {
            whitelist[msg.sender][sendersToUpdate[i]] = newStatus;
        }
    }
}
