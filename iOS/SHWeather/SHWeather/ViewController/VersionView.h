//
//  VersionView.h
//  SHWeather
//
//  Created by xiao on 6/27/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface VersionView : UIView

//- (UIView *)initWithFrame:(CGRect)frame andType:(NSString *)type andData:(NSDictionary *)data;
+(VersionView *)createViewWithType:(NSString *)type;

@end
