//
//  PassengerTripAssembly.swift
//  TinkoffApp
//
//  Created by Станислава on 17.03.2024.
//

import UIKit

final class PassengerTripAssembly {
    private let serviceAssembly: ServiceAssembly

    init(serviceAssembly: ServiceAssembly) {
        self.serviceAssembly = serviceAssembly
    }
    
    func makePassengerTripModule(moduleOutput: PassengerTripModuleOutput, model: TripModel) -> UIViewController {
        let presenter = PassengerTripPresenter(
            moduleOutput: moduleOutput,
            telemetryService: serviceAssembly.makeTelemetryService(),
            tripModel: model
        )
        let tripVC = PassengerTripViewController(output: presenter)
        presenter.viewInput = tripVC
        
        return tripVC
    }
}
