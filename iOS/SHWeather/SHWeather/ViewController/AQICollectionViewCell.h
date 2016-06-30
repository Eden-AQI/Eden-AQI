//
//  AQICollectionViewCell.h
//  SHWeather
//
//  Created by xiao on 5/15/16.
//  Copyright © 2016 xiao. All rights reserved.
//
@protocol AQICollectionViewCellDelegate <NSObject>

- (void)detailBtnAction;

@end

#import "AQIModel.h"
#import <UIKit/UIKit.h>

@interface AQICollectionViewCell : UICollectionViewCell

@property (weak, nonatomic)id<AQICollectionViewCellDelegate> delegate;

- (void)uploadDataWithModel:(AQIModel *)model;

@end
