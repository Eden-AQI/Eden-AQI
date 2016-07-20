//
//  WeatherViewModel.m
//  SHWeather
//
//  Created by xiao on 5/30/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "WeatherViewModel.h"

@implementation WeatherViewModel

+(AQIModel *)returnAQIDataFromData:(NSDictionary *)data
{
    AQIModel *model = [[AQIModel alloc]init];
    model.updateTime = [data objectForKey:@"UpdateTime"];
//    model.updateHourTime = []
    model.aqiLevel = [data objectForKey:@"AqiLevel"];
//    NSLog(@"-----Aqi:%@",[data objectForKey:@"Aqi"]);
    NSString *aqiStr = [NSString stringWithFormat:@"%@",[data objectForKey:@"Aqi"]];
    if ([aqiStr isEqualToString:@"(null)"]) {
        aqiStr = @"";
    }
    model.aqiValue = aqiStr;
    model.primaryParameter = [data objectForKey:@"PrimaryParameter"];
    model.PrimaryValue = [data objectForKey:@"PrimaryValue"];
    model.forecast = [data objectForKey:@"Forecast"];
    if (model.aqiLevel.integerValue == 1) {
        model.levelImage = [UIImage imageNamed:@"f-1.png"];
        model.actorImage = [UIImage imageNamed:@"Xiaomu_1"];
    }else if(model.aqiLevel.integerValue == 2){
        model.levelImage = [UIImage imageNamed:@"f-2.png"];
        model.actorImage = [UIImage imageNamed:@"Xiaomu_2"];
    }else if(model.aqiLevel.integerValue == 3){
        model.levelImage = [UIImage imageNamed:@"f-3.png"];
        model.actorImage = [UIImage imageNamed:@"Xiaomu_3"];
    }else if(model.aqiLevel.integerValue == 4){
        model.levelImage = [UIImage imageNamed:@"f-4.png"];
        model.actorImage = [UIImage imageNamed:@"Xiaomu_4"];
    }else if(model.aqiLevel.integerValue == 5){
        model.levelImage = [UIImage imageNamed:@"f-5.png"];
        model.actorImage = [UIImage imageNamed:@"Xiaomu_5"];
    }else if(model.aqiLevel.integerValue == 6){
        model.levelImage = [UIImage imageNamed:@"f-6.png"];
        model.actorImage = [UIImage imageNamed:@"Xiaomu_6"];
    }
    return model;
}

+(AQIDetailModel *)returnDetailDataFromData:(NSDictionary *)data
{
    AQIDetailModel *model = [[AQIDetailModel alloc]init];
    model.primaryParameter = [data objectForKey:@"PrimaryParameter"];
    NSString *string = [data objectForKey:@"PrimaryValue"];
//    NSString *str2 = [string substringWithRange:NSMakeRange(0, string.length-5)];
    model.PrimaryValue = string;
    model.aqiLevel = [data objectForKey:@"AqiLevel"];
    model.otherParameters = [NSMutableDictionary dictionaryWithCapacity:10];
    NSArray *array = [data objectForKey:@"OtherParameters"];
    for (int i=0; i<array.count; i++) {
        NSDictionary *dic = [array objectAtIndex:i];
        [model.otherParameters setValue:[dic objectForKey:@"Value"] forKey:[dic objectForKey:@"Name"]];
    }
    model.health = [data objectForKey:@"Health"];
    model.suggest = [data objectForKey:@"Suggest"];
    return model;
}

+(HoursModel *)returnHoursDataFromData:(NSDictionary *)data
{
    HoursModel *model = [[HoursModel alloc]init];
    model.twentyHoursData = [NSMutableDictionary dictionaryWithCapacity:24];
    NSArray *dataArray = [data objectForKey:@"Hours"];
    NSMutableArray *muArray = [[NSMutableArray alloc]initWithCapacity:10];
    for (int i = 0; i<dataArray.count; i++) {
        NSDictionary *itemDic = [dataArray objectAtIndex:i];
        [muArray addObject:[itemDic objectForKey:@"Parameter"]];
        [model.twentyHoursData setObject:[itemDic objectForKey:@"Data"] forKey:[itemDic objectForKey:@"Parameter"]];
    }
    model.factors = muArray;
    return model;
}

+(AQIRecordModel *)returnAQIRecordDataFromData:(NSDictionary *)data
{
    AQIRecordModel *model = [[AQIRecordModel alloc]init];
    model.daysData = [data objectForKey:@"Days"];
    return model;
}

//- (NSMutableDictionary *)muDic
//{
//    if (_muDic == nil) {
//        _muDic = [[NSMutableDictionary alloc]initWithCapacity:10];
//    }
//    return _muDic;
//}

@end
