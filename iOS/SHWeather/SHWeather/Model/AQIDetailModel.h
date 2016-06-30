//
//  AQIDetailModel.h
//  SHWeather
//
//  Created by xiao on 5/30/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AQIDetailModel : NSObject

@property (strong, nonatomic) NSString *primaryParameter;
@property (strong, nonatomic) NSString *PrimaryValue;
@property (strong, nonatomic) NSMutableDictionary *otherParameters;
@property (strong, nonatomic) NSString *health;
@property (strong, nonatomic) NSString *suggest;
@property (strong, nonatomic) NSString *aqiLevel;

@end
