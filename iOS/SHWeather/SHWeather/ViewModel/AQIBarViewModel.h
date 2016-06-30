//
//  AQIBarViewModel.h
//  SHWeather
//
//  Created by xiao on 5/17/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface AQIBarViewModel : NSObject

@property (strong, nonatomic) UIColor *capColor;
@property (nonatomic) float yValue;
@property (strong, nonatomic) NSString *xString;

- (id)initWithStandardDic:(NSDictionary *)standard;

@end
