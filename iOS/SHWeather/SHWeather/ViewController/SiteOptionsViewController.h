//
//  SiteOptionsViewController.h
//  SHWeather
//
//  Created by xiao on 6/1/16.
//  Copyright © 2016 xiao. All rights reserved.
//
@class SiteModel;
@protocol SiteOptionsViewControllerDelegate <NSObject>

- (void)insertSite:(SiteModel *)site;

@end

#import <UIKit/UIKit.h>
#import "SiteModel.h"

@interface SiteOptionsViewController : UIViewController

@property (weak, nonatomic) id<SiteOptionsViewControllerDelegate> delegate;

@end
