//
//  Global.h
//  SHWeather
//
//  Created by xiao on 5/29/16.
//  Copyright © 2016 xiao. All rights reserved.
//
typedef NS_ENUM(NSInteger,ReturnDataType)
{
    Json = 0,
    Xml = 1
};

#import <Foundation/Foundation.h>
#import "AFNetworking.h"

@interface Global : NSObject

+(NSString *)api:(NSString *)catalog;
+(AFHTTPRequestOperationManager *)createRequest;
+(AFHTTPRequestOperationManager *)createRequestWithType:(ReturnDataType)type;
@end
