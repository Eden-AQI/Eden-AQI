//
//  SiteModel.h
//  SHWeather
//
//  Created by xiao on 6/23/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SiteModel : NSObject

@property (strong, nonatomic) NSString *aqi;
@property (strong, nonatomic) NSString *backgroundImageUrl;
@property (strong, nonatomic) NSString *grade;
@property (strong, nonatomic) NSString *group;
@property (strong, nonatomic) NSString *Id;
@property (nonatomic) float latitude;
@property (nonatomic) float longitude;
@property (strong, nonatomic) NSString *name;

- (instancetype)initWithDic:(NSDictionary *)dic;

- (NSDictionary *)siteConvertToDic;
@end
