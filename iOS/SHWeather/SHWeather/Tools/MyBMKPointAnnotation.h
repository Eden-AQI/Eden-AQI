//
//  MyBMKPointAnnotation.h
//  SHWeather
//
//  Created by xiao on 6/25/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <BaiduMapAPI_Map/BMKPointAnnotation.h>

@interface MyBMKPointAnnotation : BMKPointAnnotation

@property (strong, nonatomic) NSString *profNumber;
@property (strong, nonatomic) NSString *shopID;
@end
