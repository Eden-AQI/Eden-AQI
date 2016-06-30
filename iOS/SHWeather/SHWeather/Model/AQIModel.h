//
//  AQIModel.h
//  SHWeather
//
//  Created by xiao on 5/30/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AQIModel : NSObject

@property (strong, nonatomic) NSString *updateHourTime;
@property (strong, nonatomic) NSString *updateTime;
@property (strong, nonatomic) NSString *aqiLevel;
@property (strong, nonatomic) NSString *aqiValue;
@property (strong, nonatomic) NSString *primaryParameter;
@property (strong, nonatomic) NSString *PrimaryValue;
@property (strong, nonatomic) NSArray *forecast;

@property (strong, nonatomic) UIImage *levelImage;
@property (strong, nonatomic) UIImage *actorImage;
@end
