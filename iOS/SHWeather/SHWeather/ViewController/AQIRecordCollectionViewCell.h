//
//  AQIRecordCollectionViewCell.h
//  SHWeather
//
//  Created by xiao on 5/15/16.
//  Copyright © 2016 xiao. All rights reserved.
//
#import "AQIRecordModel.h"
#import <UIKit/UIKit.h>

@interface AQIRecordCollectionViewCell : UICollectionViewCell

- (void)uploadDataWithModel:(AQIRecordModel *)model;

@end
