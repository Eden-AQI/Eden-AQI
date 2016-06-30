//
//  SiteModel.m
//  SHWeather
//
//  Created by xiao on 6/23/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "SiteModel.h"

@implementation SiteModel

- (instancetype)initWithDic:(NSDictionary *)dic
{
    if (self = [super init]) {
        self.aqi = [dic objectForKey:@"Aqi"];
        self.backgroundImageUrl = [dic objectForKey:@"BackgroundImageUrl"];
        self.grade = [dic objectForKey:@"Grade"];
        self.group = [dic objectForKey:@"Group"];
        self.Id = [NSString stringWithFormat:@"%@",[dic objectForKey:@"Id"]];
        self.latitude = [[dic objectForKey:@"Latitude"] floatValue];
        self.longitude = [[dic objectForKey:@"Longitude"] floatValue];
        self.name = [dic objectForKey:@"Name"];
    }
    return self;
}

- (NSDictionary *)siteConvertToDic
{
    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:10];
    [dic setObject:[NSString stringWithFormat:@"%@",self.aqi] forKey:@"Aqi"];
    [dic setObject:[NSString stringWithFormat:@"%@",self.backgroundImageUrl] forKey:@"BackgroundImageUrl"];
    [dic setObject:[NSString stringWithFormat:@"%@",self.grade] forKey:@"Grade"];
    [dic setObject:[NSString stringWithFormat:@"%@",self.group] forKey:@"Group"];
    [dic setObject:[NSString stringWithFormat:@"%@",self.Id] forKey:@"Id"];
    [dic setObject:[NSString stringWithFormat:@"%f",self.latitude] forKey:@"Latitude"];
    [dic setObject:[NSString stringWithFormat:@"%f",self.longitude] forKey:@"Longitude"];
    [dic setObject:[NSString stringWithFormat:@"%@",self.name] forKey:@"Name"];
    return dic;
}

@end
