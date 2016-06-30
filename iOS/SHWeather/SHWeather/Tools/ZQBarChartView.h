//
//  ZQBarChartView.h
//  SHWeather
//
//  Created by xiao on 5/16/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

@interface ZQBarChartView : UIScrollView

@property (nonatomic, assign) float yValueMax;
@property (nonatomic, strong) NSDate *time;
@property (nonatomic, strong) NSMutableArray *yValues;

@property(nonatomic,strong)UIColor *strokeColor;
@property(nonatomic,assign)NSInteger numberOfPage;
@property(nonatomic,assign)NSInteger pageCount;
//
@property(nonatomic, strong)NSMutableDictionary *yDic;
@property (nonatomic, strong) NSMutableArray *yLevels;
@property (nonatomic, strong) NSMutableArray *xLabels;
//@property (nonatomic,strong) CAShapeLayer * chartLine;

- (id)initWithFrame:(CGRect)frame yData:(NSArray *)yValues yMax:(float)yValueMax time:(NSDate *)time;

- (id)initWithFrame:(CGRect)frame yData:(NSArray *)yValues yMax:(float)yValueMax yDic:(NSDictionary *)yDic yLevels:(NSArray *)yLevels xLabels:(NSArray *)xLabels;

- (void)initialXLabels;
@end
