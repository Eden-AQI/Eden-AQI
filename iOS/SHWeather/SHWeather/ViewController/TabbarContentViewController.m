//
//  TabbarContentViewController.m
//  SHWeather
//
//  Created by xiao on 6/3/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "TabbarContentViewController.h"
#import "ZQWeatherViewController.h"
#import "ZQMapViewController.h"
#import "ZQRankListViewController.h"
#import "ZQSettingViewController.h"

@interface TabbarContentViewController ()

@end

@implementation TabbarContentViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    UITabBarController *tabbarController = [[UITabBarController alloc]init];
    
    UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, self.view.bounds.size.width, 49)];
    backView.backgroundColor = [UIColor blackColor];
    [tabbarController.tabBar insertSubview:backView atIndex:0];
    tabbarController.tabBar.opaque = YES;
    tabbarController.tabBar.tintColor = [UIColor whiteColor];
    
    ZQWeatherViewController *weather = [[ZQWeatherViewController alloc]init];
    weather.view.backgroundColor = [UIColor blueColor];
    weather.tabBarItem = [[UITabBarItem alloc]initWithTitle:@"实时" image:[UIImage imageNamed:@"tab_shishi"] tag:0];
    
//    CitiesWeatherViewController *weather = [[CitiesWeatherViewController alloc]init];
//    weather.view.backgroundColor = [UIColor blueColor];
//    weather.tabBarItem = [[UITabBarItem alloc]initWithTitle:@"实时" image:[UIImage imageNamed:@"sy.png"] tag:0];
    
    ZQMapViewController *map = [[ZQMapViewController alloc]init];
//    map.view.backgroundColor = [UIColor redColor];
    map.tabBarItem = [[UITabBarItem alloc]initWithTitle:@"地图" image:[UIImage imageNamed:@"tab_map"] tag:1];
    
    ZQRankListViewController *rankList = [[ZQRankListViewController alloc]init];
    rankList.view.backgroundColor = [UIColor yellowColor];
    rankList.tabBarItem = [[UITabBarItem alloc]initWithTitle:@"列表" image:[UIImage imageNamed:@"tab_list"] tag:2];
    
    ZQSettingViewController *setting = [[ZQSettingViewController alloc]init];
//    setting.view.backgroundColor = [UIColor orangeColor];
    setting.tabBarItem = [[UITabBarItem alloc]initWithTitle:@"设置" image:[UIImage imageNamed:@"tab_setting"] tag:3];
    [tabbarController setViewControllers:@[weather,map,rankList,setting]];
    
    [self addChildViewController:tabbarController];
    tabbarController.view.frame = self.view.bounds;
    [self.view addSubview:tabbarController.view];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
