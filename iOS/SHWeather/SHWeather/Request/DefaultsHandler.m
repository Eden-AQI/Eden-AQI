//
//  DefaultsHandler.m
//  SHWeather
//
//  Created by xiao on 6/10/16.
//  Copyright © 2016 xiao. All rights reserved.
//
#define UserDefaults [NSUserDefaults standardUserDefaults]
#import "DefaultsHandler.h"
#import "SiteModel.h"

@interface DefaultsHandler ()

@end

@implementation DefaultsHandler

+ (instancetype)sharedInstance
{
    static id instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc]init];
    });
    return instance;
}

+ (BOOL)editPairWithData:(id)data withKey:(NSString *)key
{
    
    if ([data isKindOfClass:[SiteModel class]]) {
        SiteModel *model = (SiteModel *)data;
        if ([key isEqualToString:FOLLOWCITIES]) {
            NSArray *array = [NSArray arrayWithObjects:[model siteConvertToDic], nil];
            [UserDefaults setObject:array forKey:key];
            return [UserDefaults synchronize];
        }
        [UserDefaults setObject:[model siteConvertToDic] forKey:key];
        return [UserDefaults synchronize];
    }else{
        [UserDefaults setObject:data forKey:key];
        return [UserDefaults synchronize];
    }
}

+ (BOOL)updateItems:(NSArray *)items forKey:(NSString *)key
{
    if([key isEqualToString:FOLLOWCITIES]){
        NSMutableArray *muItems = [NSMutableArray arrayWithCapacity:10];
        for (SiteModel *model in items) {
            NSDictionary *dic = [model siteConvertToDic];
            [muItems addObject:dic];
        }
        [UserDefaults setValue:muItems forKey:key];
        return [UserDefaults synchronize];
    }
    [UserDefaults setValue:items forKey:key];
    return [UserDefaults synchronize];
}

+ (BOOL)checkItemExistIfNotInsert:(id)data forKey:(NSString *)key
{
    if ([data isKindOfClass:[SiteModel class]]) {
        SiteModel *model = (SiteModel *)data;
        NSDictionary *dic = [model siteConvertToDic];
        NSMutableArray *muArray = [NSMutableArray arrayWithArray:[UserDefaults objectForKey:key]];
        if ([muArray containsObject:dic]) {
            return YES;
        }
        [muArray addObject:dic];
        [UserDefaults setValue:muArray forKey:key];
    }else{
        NSMutableArray *muArray = [NSMutableArray arrayWithArray:[UserDefaults objectForKey:key]];
        if ([muArray containsObject:data]) {
            return YES;
        }
        [muArray addObject:data];
        [UserDefaults setValue:muArray forKey:key];
    }
    return [UserDefaults synchronize];
}

+ (id)searchDataForKey:(NSString *)key
{
    if ([key isEqualToString:LOCATIONSITE]||[key isEqualToString:DEFAULTSITE]) {
        SiteModel *model = [[SiteModel alloc]initWithDic:[UserDefaults objectForKey:key]];
        return model;
    }else if([key isEqualToString:FOLLOWCITIES]){
        NSArray *muArray = [NSArray arrayWithArray:[UserDefaults objectForKey:key]];
        NSMutableArray *siteModels = [NSMutableArray arrayWithCapacity:10];
        for (int i=0; i<muArray.count; i++) {
            NSDictionary *itemDic = [muArray objectAtIndex:i];
            SiteModel *model = [[SiteModel alloc]initWithDic:itemDic];
            [siteModels addObject:model];
        }
        return siteModels;
    }
    return [UserDefaults objectForKey:key];
}

//
+ (BOOL)celarUserDefaults
{
    [DefaultsHandler editPairWithData:nil withKey:DEFAULTSITE];
    [DefaultsHandler editPairWithData:nil withKey:FOLLOWCITIES];
    [DefaultsHandler editPairWithData:nil withKey:LOCATIONSITE];
    return [UserDefaults synchronize];
}

//
+ (NSDictionary *)getAirQualityLevel
{
    NSString *path = [[NSBundle mainBundle]pathForResource:@"airqualitylevel" ofType:@"plist"];
    return [NSDictionary dictionaryWithContentsOfFile:path];
}

+ (UIColor *)getColorOrderLevel:(NSString *)levelString
{
    //NSLog(@"----it should be a intVale:%@",levelString);
    UIColor *color = [UIColor clearColor];
    
    DefaultsHandler *defaults = [DefaultsHandler sharedInstance];
    NSDictionary *itemDic = [defaults.gradeInfo objectForKey:[NSNumber numberWithInt:levelString.intValue]];
    
    float colorB = [[itemDic objectForKey:@"ColorB"] floatValue]/255;
    float colorG = [[itemDic objectForKey:@"ColorG"] floatValue]/255;
    float colorR = [[itemDic objectForKey:@"ColorR"] floatValue]/255;
    color = [UIColor colorWithRed:colorR green:colorG blue:colorB alpha:0.8];
//    if ([levelString isEqualToString:@"1"]) {
//        color = [UIColor greenColor];
//    }else if([levelString isEqualToString:@"2"]){
//        color = [UIColor colorWithRed:215.0/255 green:206.0/255 blue:48.0/255 alpha:1.0];
//    }else if([levelString isEqualToString:@"3"]){
//        color = [UIColor orangeColor];
//    }else if([levelString isEqualToString:@"4"]){
//        color = [UIColor redColor];
//    }else if([levelString isEqualToString:@"5"]){
//        color = [UIColor purpleColor];
//    }
    return color;
}

+ (UIColor *)getColorOrderAQIValue:(NSString *)aqiValue
{
    DefaultsHandler *defaults = [DefaultsHandler sharedInstance];
    UIColor *color = [UIColor clearColor];
    float aqi = aqiValue.floatValue;
    for(NSDictionary *dic in defaults.gradeInfo.allValues){
        float max= [[dic objectForKey:@"AQIMax"] floatValue];
        float min= [[dic objectForKey:@"AQIMin"] floatValue];
        if (aqi>=min && aqi<max) {
            color = [DefaultsHandler getColorOrderLevel:[dic objectForKey:@"Grade"]];
            break;
        }
    }
    return color;
}

+ (NSDictionary *)getDescriptionOrderLevel:(NSString *)levelString
{
    DefaultsHandler *defaults = [DefaultsHandler sharedInstance];
    //NSLog(@"---222-it should be a intVale:%@",levelString);
    NSDictionary *itemDic = [defaults.gradeInfo objectForKey:[NSNumber numberWithInt:levelString.intValue]];
    return itemDic;
}
@end
