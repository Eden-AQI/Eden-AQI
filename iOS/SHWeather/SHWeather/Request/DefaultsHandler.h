//
//  DefaultsHandler.h
//  SHWeather
//
//  Created by xiao on 6/10/16.
//  Copyright © 2016 xiao. All rights reserved.
//
#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface DefaultsHandler : NSObject

@property (strong, nonatomic) NSDictionary *gradeInfo;
@property (strong, nonatomic) NSDictionary *siteListInfo;
@property (strong, nonatomic) NSDictionary *siteListDic;

@property (strong, nonatomic) NSString *mainSiteAqi;

+ (instancetype)sharedInstance;

+ (BOOL)checkItemExistIfNotInsert:(id)data forKey:(NSString *)key;
+ (BOOL)updateItems:(NSArray *)items forKey:(NSString *)key;
+ (BOOL)editPairWithData:(id)data withKey:(NSString *)key;
+ (id)searchDataForKey:(NSString *)key;

+ (BOOL)celarUserDefaults;

//
+ (NSDictionary *)getAirQualityLevel;
+ (UIColor *)getColorOrderLevel:(NSString *)levelString;
+ (UIColor *)getColorOrderAQIValue:(NSString *)aqiValue;
+ (NSDictionary *)getDescriptionOrderLevel:(NSString *)levelString;
+ (NSString *)getLevelNameOrderLevel:(NSString *)levelString;
@end
