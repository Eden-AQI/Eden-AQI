//
//  AppDelegate.m
//  SHWeather
//
//  Created by xiao on 5/14/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <ShareSDK/ShareSDK.h>
#import <ShareSDKConnector/ShareSDKConnector.h>
//腾讯开放平台（对应QQ和QQ空间）SDK头文件
#import <TencentOpenAPI/TencentOAuth.h>
#import <TencentOpenAPI/QQApiInterface.h>
//微信SDK头文件
#import "WXApi.h"
//新浪微博SDK头文件
#import "WeiboSDK.h"
//新浪微博SDK需要在项目Build Settings中的Other Linker Flags添加"-ObjC"

#import "AppDelegate.h"
#import "SiteManagerViewController.h"
#import "TabbarContentViewController.h"
#import "Global.h"
#import "SvUDIDTools.h"
#import "XMLDictionary.h"
#import "SiteModel.h"

@interface AppDelegate ()


@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    //baidu map
    self.mapManager = [[BMKMapManager alloc]init];
    BOOL ret = [self.mapManager start:@"cWkiwyoT6XngL4Et8kbNVO2N6aFzgv9e" generalDelegate:nil];//@"K6UtfgxcDLL8bqB795ZqZgF4kkxqBxzK"
    if (!ret) {
        NSLog(@"manager start failed");
    }
    //shareSDK
    /**
     *  设置ShareSDK的appKey，如果尚未在ShareSDK官网注册过App，请移步到http://mob.com/login 登录后台进行应用注册
     *  在将生成的AppKey传入到此方法中。
     *  方法中的第二个第三个参数为需要连接社交平台SDK时触发，
     *  在此事件中写入连接代码。第四个参数则为配置本地社交平台时触发，根据返回的平台类型来配置平台信息。
     *  如果您使用的时服务端托管平台信息时，第二、四项参数可以传入nil，第三项参数则根据服务端托管平台来决定要连接的社交SDK。
     */
    //App Key: 13cda8580a8ca
    //App Secret: 49fda3f3b7196ae56c92a824f6248a25
    
    [ShareSDK registerApp:@"13cda8580a8ca"
     
          activePlatforms:@[
                            @(SSDKPlatformTypeSinaWeibo),
                            @(SSDKPlatformTypeWechat),
                            @(SSDKPlatformTypeQQ)]
                 onImport:^(SSDKPlatformType platformType)
     {
         switch (platformType)
         {
             case SSDKPlatformTypeWechat:
                 [ShareSDKConnector connectWeChat:[WXApi class]];
                 break;
             case SSDKPlatformTypeQQ:
                 [ShareSDKConnector connectQQ:[QQApiInterface class] tencentOAuthClass:[TencentOAuth class]];
                 break;
             case SSDKPlatformTypeSinaWeibo:
                 [ShareSDKConnector connectWeibo:[WeiboSDK class]];
                 break;
             default:
                 break;
         }
     }
          onConfiguration:^(SSDKPlatformType platformType, NSMutableDictionary *appInfo)
     {
         
         switch (platformType)
         {
             case SSDKPlatformTypeSinaWeibo:
                 //设置新浪微博应用信息,其中authType设置为使用SSO＋Web形式授权
                 [appInfo SSDKSetupSinaWeiboByAppKey:@"568898243"
                                           appSecret:@"38a4f8204cc784f81f9f0daaf31e02e3"
                                         redirectUri:@"http://www.sharesdk.cn"
                                            authType:SSDKAuthTypeBoth];
                 break;
             case SSDKPlatformTypeWechat:
                 [appInfo SSDKSetupWeChatByAppId:@"wxcca2457aef1af9fc"
                                       appSecret:@"3bf952630610c67383bdfc443ebfcaf2"];
                 break;
             case SSDKPlatformTypeQQ:
                 [appInfo SSDKSetupQQByAppId:@"1105502216"
                                      appKey:@"omocWzA2MDO5HGAg"
                                    authType:SSDKAuthTypeBoth];
                 break;
             default:
                 break;
         }
     }];

    //----
//    [DefaultsHandler celarUserDefaults];

    if ([[DefaultsHandler searchDataForKey:FOLLOWCITIES] count]<1) {
        SiteModel *site = [[SiteModel alloc]init];
        site.name = @"郑州市";
        site.Id = @"0";
        site.grade = @"1";
        [DefaultsHandler editPairWithData:site withKey:FOLLOWCITIES];
        [DefaultsHandler editPairWithData:site withKey:DEFAULTSITE];
    }
    [self loadPreConfigs];
//    [self prepareZhengZhouData];
    [self prepareSiteInfoListDataWithComplete:nil];
    
    self.window = [[UIWindow alloc]initWithFrame:[UIScreen mainScreen].bounds];
    
    TabbarContentViewController *tabbarContent = [[TabbarContentViewController alloc]init];
    
    self.revealSideViewController = [[PPRevealSideViewController alloc] initWithRootViewController:tabbarContent];
    self.revealSideViewController.delegate = self;
    SiteManagerViewController *siteManager = [[SiteManagerViewController alloc]init];
    [self.revealSideViewController preloadViewController:siteManager forSide:PPRevealSideDirectionLeft];
    [self.revealSideViewController setDirectionsToShowBounce:PPRevealSideDirectionNone];
    [self.revealSideViewController setPanInteractionsWhenClosed:PPRevealSideInteractionContentView | PPRevealSideInteractionNavigationBar];
    
    self.nav = [[UINavigationController alloc]initWithRootViewController:self.revealSideViewController];
    self.window.rootViewController = self.nav;
    
    self.nav.navigationBarHidden=YES;
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent animated:YES];
    
    [self.window makeKeyAndVisible];
    
    return YES;
}

- (void)loadPreConfigs
{    
    NSString *api = [Global api:[NSString stringWithFormat:@"Metadata/GradeInfo"]];
    AFHTTPRequestOperationManager *request = [Global createRequestWithType:Xml];
    [request GET:api parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"---url:%@",operation.request.URL.absoluteString);
        NSArray *responseArray = (NSArray *)responseObject;
        NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:10];
        for (int i = 0; i<responseArray.count; i++) {
            NSDictionary *itemDic = [responseArray objectAtIndex:i];
            [dic setObject:itemDic forKey:[itemDic objectForKey:@"Grade"]];
        }
        [DefaultsHandler sharedInstance].gradeInfo = dic;
        if(dic){
            [[NSNotificationCenter defaultCenter]postNotificationName:GETGRADEINFO_NOTI object:nil];
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"---url:%@",operation.request.URL.absoluteString);
        [[[UIAlertView alloc] initWithTitle:@"提示" message:@"数据加载失败" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles: nil] show];
    }];
    //heartbeat
    NSString *deviceNumber = [SvUDIDTools UDID];
    NSLog(@"---deviceNumber:%@",deviceNumber);
    NSString *heartBeat = [Global api:[NSString stringWithFormat:@"Device/Heartbeat?deviceNumber=%@",deviceNumber]];
    [request POST:heartBeat parameters:nil success:^(AFHTTPRequestOperation * _Nonnull operation, id  _Nonnull responseObject) {
        //NSLog(@"---url:%@",operation.request.URL.absoluteString);
    } failure:^(AFHTTPRequestOperation * _Nullable operation, NSError * _Nonnull error) {
        //NSLog(@"---url:%@",operation.request.URL.absoluteString);
        [[[UIAlertView alloc] initWithTitle:@"提示" message:@"数据加载失败" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles: nil] show];
    }];
    
}
/*
- (void)prepareZhengZhouData
{
    NSString *api = [Global api:@"Aqi/GetRealtime?siteId=0"];
    AFHTTPRequestOperationManager *request = [Global createRequest];
    [request GET:api parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [DefaultsHandler sharedInstance].mainSiteAqi = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Aqi"]];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    
    }];
}*/

- (void)prepareSiteInfoListDataWithComplete:(void(^)(BOOL, NSDictionary *))completeBlock
{
    NSString *api = [Global api:[NSString stringWithFormat:@"Aqi/GetCityList"]];
    AFHTTPRequestOperationManager *request = [Global createRequest];
    [request GET:api parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSArray *responseArray = (NSArray *)responseObject;
        NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:10];
        NSMutableDictionary *citiesNameDic = [NSMutableDictionary dictionaryWithCapacity:10];
        for (int i = 0; i<responseArray.count; i++) {
            NSDictionary *itemDic = [responseArray objectAtIndex:i];
            NSArray *cityDataArray = [itemDic objectForKey:@"Items"];
            [dic setObject:cityDataArray forKey:[itemDic objectForKey:@"GroupName"]];
            NSMutableArray *muArray = [NSMutableArray arrayWithCapacity:10];
            for (int j = 0; j<cityDataArray.count; j++) {
                NSDictionary *cityDic = [cityDataArray objectAtIndex:j];
                [muArray addObject:[cityDic objectForKey:@"Name"]];
            }
            [citiesNameDic setObject:muArray forKey:[itemDic objectForKey:@"GroupName"]];
        }
        [DefaultsHandler sharedInstance].siteListInfo = dic;
        [DefaultsHandler sharedInstance].siteListDic = citiesNameDic;
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [[[UIAlertView alloc] initWithTitle:@"提示" message:@"数据加载失败" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles: nil] show];
    }];
}

#pragma -mark location
- (void)locationAction
{
//    [DefaultsHandler editPairWithData:@"YES" withKey:LOCATIONCHANGED];
    if (!self.locService) {
        self.locService = [[BMKLocationService alloc]init];
        self.locService.delegate = self;
    }
    [self.locService startUserLocationService];
}

#pragma -mark BMKLocationServiceDelegate
- (void)didUpdateUserHeading:(BMKUserLocation *)userLocation
{
    NSLog(@"didUpdateUserHeading lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
    NSLog(@"heading is %@",userLocation.heading);
}

/**
 *用户位置更新后，会调用此函数
 *@param userLocation 新的用户位置
 */
- (void)didUpdateBMKUserLocation:(BMKUserLocation *)userLocation
{
    NSLog(@"didUpdateBMKUserLocation lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
    CLGeocoder *geocoder = [[CLGeocoder alloc]init];
    CLGeocodeCompletionHandler handler = ^(NSArray *place,NSError *error){
        for(CLPlacemark *placemark in place){
            NSString *cityStr = placemark.thoroughfare;
            NSString *cityName = placemark.subLocality;
//            if ([[DefaultsHandler searchDataForKey:LOCATIONCHANGED] isEqualToString:@"YES"]) {
                NSLog(@"--Appdelegate--cityStr:%@-----cityName:%@",cityStr,cityName);
//                NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:cityName,@"cityName",userLocation,@"userLocation", nil];
                SiteModel *site = [[SiteModel alloc]init];
                site.latitude = userLocation.location.coordinate.latitude;
                site.longitude = userLocation.location.coordinate.longitude;
                site.Id = @"9999";
                site.name = cityName;
                [[NSNotificationCenter defaultCenter]postNotificationName:LOCATION_NOTI object:site];
                [self.locService stopUserLocationService];
//            }
            break;
        }
    };
    CLLocation *loc = [[CLLocation alloc]initWithLatitude:userLocation.location.coordinate.latitude longitude:userLocation.location.coordinate.longitude];
    [geocoder reverseGeocodeLocation:loc completionHandler:handler];
}

#pragma mark - PPRevealSideViewController delegate

- (void)pprevealSideViewController:(PPRevealSideViewController *)controller willPushController:(UIViewController *)pushedController {
    PPRSLog(@"%@", pushedController);
    //    [UIView animateWithDuration:0.3
    //                     animations:^{
    //                         _iOS7UnderStatusBar.alpha = 1.0;
    //                     }];
}

- (void)pprevealSideViewController:(PPRevealSideViewController *)controller didPushController:(UIViewController *)pushedController {
    PPRSLog(@"%@", pushedController);
}

- (void)pprevealSideViewController:(PPRevealSideViewController *)controller willPopToController:(UIViewController *)centerController {
    PPRSLog(@"%@", centerController);
    //    [UIView animateWithDuration:0.3
    //                     animations:^{
    //                         _iOS7UnderStatusBar.alpha = 0.0;
    //                     }];
}

- (void)pprevealSideViewController:(PPRevealSideViewController *)controller didPopToController:(UIViewController *)centerController {
    PPRSLog(@"%@", centerController);
}

- (void)pprevealSideViewController:(PPRevealSideViewController *)controller didChangeCenterController:(UIViewController *)newCenterController {
    PPRSLog(@"%@", newCenterController);
}

- (BOOL)pprevealSideViewController:(PPRevealSideViewController *)controller shouldDeactivateDirectionGesture:(UIGestureRecognizer *)gesture forView:(UIView *)view {
    return NO;
}

- (PPRevealSideDirection)pprevealSideViewController:(PPRevealSideViewController *)controller directionsAllowedForPanningOnView:(UIView *)view {
    if ([view isKindOfClass:NSClassFromString(@"UIWebBrowserView")])
        
        return PPRevealSideDirectionLeft; //| PPRevealSideDirectionRight;
    
    return PPRevealSideDirectionLeft;// | PPRevealSideDirectionRight | PPRevealSideDirectionTop | PPRevealSideDirectionBottom;
}

- (void)pprevealSideViewController:(PPRevealSideViewController *)controller didManuallyMoveCenterControllerWithOffset:(CGFloat)offset
{
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
