import SwiftUI
import ComposeApp
import NSExceptionKtCrashlytics

@main
struct iOSApp: App {

    init() {
        MainViewControllerKt.initialize()
        NSExceptionKt.addReporter(.crashlytics(causedByStrategy: .append))
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
