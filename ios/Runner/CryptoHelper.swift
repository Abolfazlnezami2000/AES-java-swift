import Foundation
import CryptoSwift

var keyValue :String!

class CryptoHelper{
    private static let iv = "RF22SW76BV83EDH8";
    // ENC
    public static func encrypt(dataFromFlutter :String, keyFromFlutter :String) -> String? {
        do{
            let encrypted: Array<UInt8> = try AES(key: keyFromFlutter, iv: iv, padding: .pkcs5).encrypt(Array(dataFromFlutter.utf8))
            return encrypted.toBase64()
        }catch{
            return "DATA ERROR"
        }
    }

    // DEC
    public static func decrypt(dataFromFlutter :String, keyFromFlutter :String) -> String? {
        do{
            let data = Data(base64Encoded: dataFromFlutter)
            let decrypted = try AES(key: keyFromFlutter, iv: iv, padding: .pkcs5).decrypt(data!.bytes)
            return String(data: Data(decrypted), encoding: .utf8)
        }catch{
            return "DATA ERROR"
        }
    }
}