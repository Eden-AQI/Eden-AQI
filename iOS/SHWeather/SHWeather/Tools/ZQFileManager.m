//
//  ZQFileManager.m
//  SHWeather
//
//  Created by xiao on 6/4/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "ZQFileManager.h"

@interface ZQFileManager ()

@property (strong, nonatomic) NSFileManager *fileManager;

@end

@implementation ZQFileManager

+ (instancetype)sharedInstance
{
    static id instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc]init];
    });
    return instance;
}

- (instancetype)init{
    if (self = [super init]) {
        self.fileManager = [NSFileManager defaultManager];
    }
    return self;
}
//delete
- (BOOL)storedCityToPlistWithCityName:(NSString *)cityName
{
    NSArray *docPaths=NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask,YES);
    NSString *docDir=[docPaths objectAtIndex:0];
    //if user exist ,create user's folder
    NSLog(@"----docDir:%@",docDir);
    BOOL isDir = [docDir isAbsolutePath];
    if ([self.fileManager fileExistsAtPath:docDir isDirectory:&isDir]) {
        NSString *string = [NSString stringWithContentsOfFile:docDir encoding:NSUTF8StringEncoding error:nil];
        NSLog(@"---hehe string:%@",string);
    }else{
        [self.fileManager createDirectoryAtPath:docDir withIntermediateDirectories:YES attributes:nil error:nil];
    }
    return [cityName writeToFile:docDir atomically:YES encoding:NSUTF8StringEncoding error:nil];
}

@end
