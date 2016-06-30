//
//  WeatherViewModel.h
//  SHWeather
//
//  Created by xiao on 5/30/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AQIModel.h"
#import "AQIDetailModel.h"
#import "HoursModel.h"
#import "AQIRecordModel.h"

@interface WeatherViewModel : NSObject

+(AQIModel *)returnAQIDataFromData:(NSDictionary *)data;
+(AQIDetailModel *)returnDetailDataFromData:(NSDictionary *)data;
+(HoursModel *)returnHoursDataFromData:(NSDictionary *)data;
+(AQIRecordModel *)returnAQIRecordDataFromData:(NSDictionary *)data;

@end
