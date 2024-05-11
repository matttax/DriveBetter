import SwiftUI
import shared
import YandexMapsMobile

@main
struct iOSApp: App {

    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        KoinStarterKt.doInitKoin()
        YMKMapKit.setApiKey("46524574-c032-4d49-8c7c-5e7c8709543e")
        YMKMapKit.sharedInstance().onStart()
        return true
    }
}
