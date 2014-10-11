//
//  ViewController.m
//  SmartCard
//
//  Created by Administrator on 11.10.14.
//  Copyright (c) 2014 Administrator. All rights reserved.
//

#import "ViewController.h"

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
    
    //CBPeripheralManager myPeripheralManager;
    
    NSString *server = @"http://hackzurich14.worx.li/setProfile.php";
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc]
                                    initWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@",server]]];

    NSString *postString = [NSString stringWithFormat:@"uid=-1&name=%@&firstname=%@&email=%@",_name.text,_firstname.text,_email.text];
    
    NSLog(postString);
    
    [request setHTTPMethod: @"POST" ];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"content-type"];
    [request setHTTPBody:[postString dataUsingEncoding:NSUTF8StringEncoding]];
    
    NSURLResponse *response;
    NSError *err;
    NSData *returnData = [ NSURLConnection sendSynchronousRequest: request returningResponse:&response error:&err];
    NSString *content = [NSString stringWithUTF8String:[returnData bytes]];
    NSLog(@"responseData: %@", content);
    
    
    //myPeripheralManager = [[CBPeripheralManager alloc] initWithDelegate:self queue:nil options:nil];
    
    
    
    
    
}
@end
