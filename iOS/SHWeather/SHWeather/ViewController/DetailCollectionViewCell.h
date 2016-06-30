//
//  DetailCollectionViewCell.h
//  SHWeather
//
//  Created by xiao on 5/15/16.
//  Copyright © 2016 xiao. All rights reserved.
//
#import "AQIDetailModel.h"
#import <UIKit/UIKit.h>

@interface DetailCollectionViewCell : UICollectionViewCell<UITableViewDelegate,UITableViewDataSource>

- (void)uploadDataWithModel:(AQIDetailModel *)model;

@end
