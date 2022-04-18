import Foundation

@objc public class Browser: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
