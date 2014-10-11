//
//  ViewController.h
//  SmartCard
//
//  Created by Administrator on 11.10.14.
//  Copyright (c) 2014 Administrator. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreBluetooth/CoreBluetooth.h>
#import <CoreLocation/CoreLocation.h>

@interface ViewController : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *name;
@property (weak, nonatomic) IBOutlet UITextField *firstname;
@property (weak, nonatomic) IBOutlet UITextField *email;

@property (nonatomic, retain) CLBeaconRegion *beaconRegion;
@property (nonatomic, retain) CBPeripheralManager *BLEmanager;

- (IBAction)share:(id)sender;

@end

