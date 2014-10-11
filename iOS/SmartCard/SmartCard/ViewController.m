//
//  ViewController.m
//  SmartCard
//
//  Created by Administrator on 11.10.14.
//  Copyright (c) 2014 Administrator. All rights reserved.
//

#import "ViewController.h"
#import "AFNetworking/AFNetworking.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)share:(id)sender {
    
    NSLog(@"start");
    
    NSString *server = @"http://hackzurich14.worx.li/setProfile.php";
    
    
    ////
    
    NSLog(@"start2");

    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSDictionary *parameters = @{@"name": self.name.text, @"first_name": self.firstname.text, @"email": self.email.text, @"UUID": @"1112", @"UID": @"-1"};
    [manager POST:[NSString stringWithFormat:@"%@", server] parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"JSON: %@", responseObject);
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error: %@", error);
    }];
    
    NSLog(@"start3");

    
    
    
    _BLEmanager = [[CBPeripheralManager alloc] initWithDelegate:self queue:nil options:nil];
    
    /* Initialization */
    NSUUID *uuid = [[NSUUID alloc] initWithUUIDString:@"SOME-UUID-STRING-HERE"];
    NSString *identifier = @"MyBeacon";
    //Construct the region
    self.beaconRegion = [[CLBeaconRegion alloc] initWithProximityUUID:uuid identifier:identifier];
    self.BLEmanager = [[CBPeripheralManager alloc] initWithDelegate:self queue:nil];
    
    
}


#pragma mark - CBPeripheralManagerDelegate Methods

//CBPeripheralManager callback once the manager is ready to accept commands
- (void)peripheralManagerDidUpdateState:(CBPeripheralManager *)peripheral
{
    if (peripheral.state != CBPeripheralManagerStatePoweredOn) {
        return;
    }
    
    //Passing nil will use the device default power
    NSDictionary *payload = [_beaconRegion peripheralDataWithMeasuredPower:nil];
    
    //Start advertising
    [_BLEmanager startAdvertising:payload];
}


@end
