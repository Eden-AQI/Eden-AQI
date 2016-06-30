//
//  Config.h
//  SHWeather
//
//  Created by xiao on 5/15/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#ifndef Config_h
#define Config_h

#define SCREEN_WIDTH [UIApplication sharedApplication].keyWindow.frame.size.width
#define SCREEN_HEIGHT [UIApplication sharedApplication].keyWindow.frame.size.height

//userdefaults
#define FOLLOWCITIES @"followCities"
#define LOCATIONSITE @"locationSite"
#define DEFAULTSITE @"defaultSite"
#define DEVICENUMBER @"deviceNumber"
//#define CURRENTVERSION @"currentVersion"
//#define LOCATIONCHANGED @"locationChanged"

//notifications
#define LOCATION_NOTI   @"locationNotification"
#define CHANGESITE_NOTI   @"changeSiteNotification"
#define INSERTSITE_NOTI   @"insertSiteNotification"
#define DELETESITE_NOTI   @"deleteSiteNotification"
#define GETGRADEINFO_NOTI   @"getGradeInfo"

//
#define checkStringNull(Str) Str == [NSNull null] || Str == nil ? @”” : [NSString stringWithFormat:@”%@”, Str]
#endif /* Config_h */
