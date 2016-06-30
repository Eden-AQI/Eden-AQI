//
//  AppDelegate.h
//  SHWeather
//
//  Created by xiao on 5/14/16.
//  Copyright © 2016 xiao. All rights reserved.
//
#import <BaiduMapAPI_Base/BMKMapManager.h>
#import <BaiduMapAPI_Location/BMKLocationService.h>
#import <UIKit/UIKit.h>
#import "PPRevealSideViewController.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate,BMKLocationServiceDelegate,PPRevealSideViewControllerDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) UINavigationController *nav;
@property (strong, nonatomic) PPRevealSideViewController *revealSideViewController;
@property (strong, nonatomic) BMKMapManager *mapManager;
@property (strong, nonatomic) BMKLocationService *locService;

- (void)locationAction;

- (void)prepareSiteInfoListDataWithComplete:(void(^)(BOOL, NSDictionary *))completeBlock;
//- (void)locationActionWithCompletion:(void(^)(NSString *,BMKUserLocation *))completion;

@end

