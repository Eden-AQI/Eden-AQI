//
//  ZQFileManager.h
//  SHWeather
//
//  Created by xiao on 6/4/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ZQFileManager : NSObject

+ (instancetype)sharedInstance;

- (BOOL)storedCityToPlistWithCityName:(NSString *)cityName;

@end
