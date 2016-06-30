//
//  ZQLineChartView.h
//  SHWeather
//
//  Created by xiao on 5/16/16.
//  Copyright © 2016 xiao. All rights reserved.
//
typedef NS_ENUM(NSInteger,returnTime_type)
{
    day=0,
    month,
    year,
    recently24h,
    recently15d,
};

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

@interface ZQLineChartView : UIScrollView
@property(nonatomic)returnTime_type time_type;

@property(nonatomic,strong)UIColor *strokeColor;
@property(nonatomic,strong)NSDictionary *standardLines;

@property(nonatomic,strong)NSArray *xLabels;
@property(nonatomic,strong)NSMutableArray *yValues;
@property(nonatomic,strong)NSArray *yLabels;
@property(nonatomic,strong)NSDictionary *yIndexDic;
@property(nonatomic,strong)NSMutableDictionary *yDic;

@property(nonatomic,strong)NSArray *linesArray;
@property(nonatomic,strong)NSArray *yPositionAry;

@property(nonatomic,assign)float yValueMax;
@property(nonatomic,strong)NSDate *startTime;

@property(nonatomic,assign)BOOL showPoint;
@property(nonatomic,assign)BOOL needGrid;
@property(nonatomic,assign)BOOL enableRoll;
@property(nonatomic,assign)NSInteger numberOfPage;
@property(nonatomic,assign)NSInteger pageCount;

@property (nonatomic,strong) CAShapeLayer * chartLine;

- (id)initWithFrame:(CGRect)frame type:(int)time_type yData:(NSArray *)yValues yDic:(NSDictionary *)yDic  yMax:(float)yValueMax xLabels:(NSArray *)xLabels;

- (id)initWithFrame:(CGRect)frame Color:(UIColor *)strokeColor type:(int)time_type standard:(NSDictionary *)standardValues ydata:(NSArray *)yValues  yMax:(float)yValueMax xlabels:(NSArray *)xlabels yDic:(NSDictionary *)yDic startTime:(NSDate *)startTime yIndexAccor:(NSDictionary *)yIndexDic;

- (void)initialXLabels;
@end

