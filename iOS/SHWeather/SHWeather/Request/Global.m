//
//  Global.m
//  SHWeather
//
//  Created by xiao on 5/29/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "Global.h"

@implementation Global

+(NSString *)api:(NSString *)catalog{
    return [NSString stringWithFormat:@"http://aqi.wuhooooo.com/api/%@",catalog];
}

+(AFHTTPRequestOperationManager *)createRequest{
    AFHTTPRequestOperationManager *manager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:nil];
    
    manager.responseSerializer = [AFJSONResponseSerializer serializerWithReadingOptions:NSJSONReadingMutableContainers];;
    
    manager.requestSerializer=[AFJSONRequestSerializer serializer];
    manager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"application/json", nil];

    return  manager;
}

+(AFHTTPRequestOperationManager *)createRequestWithType:(ReturnDataType)type
{
    AFHTTPRequestOperationManager *manager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:nil];
    
//    manager.responseSerializer = [AFXMLParserResponseSerializer serializer];
    
//    manager.requestSerializer.timeoutInterval = 10;
    manager.responseSerializer.acceptableContentTypes=[NSSet setWithObjects:@"application/json", @"text/json", @"text/javascript",@"text/html", nil];

    return  manager;
}

@end
