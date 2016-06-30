//
//  ZQWeatherViewController.h
//  SHWeather
//
//  Created by xiao on 5/14/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ZQWeatherViewController : UIViewController<UICollectionViewDelegateFlowLayout,UICollectionViewDelegate,UICollectionViewDataSource>

@property (nonatomic) NSInteger pageIndex;
@property (nonatomic, strong) NSArray *data;

- (void)reloadData;

@end
