//
//  MyIdModule.swift
//  MyIdSample
//

import Foundation
import React
import MyIdSDK

@objc(MyIdModule)
class MyIdModule: RCTEventEmitter {
  
  @objc
  static func constantsToExport() -> [String: Any]{
    return ["initialCount": 0]
  }
  
  @objc
  override static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  @objc
  override func supportedEvents() -> [String]! {
    return ["onSuccess", "onError", "onUserExited"]
  }
  
  @objc
  func startMyId() {
    DispatchQueue.main.asyncAfter(deadline: .now() + 0.1, execute: {
      MyIdSdk.start(withConfigureOptions: { options in
        options?.clientId = "clientId"
        options?.entryType = .AUTH
      }, withDelegate: self)
    })
  }
}

extension MyIdModule: MyIdSdkDelegate {

  func myidOnSuccess(result: MyIdResult) {
    sendEvent(
      withName: "onSuccess",
      body: [
        "code": result.code,
        "comparison": result.comparisonValue
      ]
    )
  }
  
  func myidOnError(exception: MyIdException) {
    sendEvent(
      withName: "onError",
      body: [
        "message": exception.message,
        "code": exception.code
      ]
    )
  }
  
  func myidOnUserExited() {
    sendEvent(
      withName: "onUserExited",
      body: [
        "message": "exited"
      ]
    )
  }
}







