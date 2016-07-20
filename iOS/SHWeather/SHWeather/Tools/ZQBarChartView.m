//
//  ZQBarChartView.m
//  SHWeather
//
//  Created by xiao on 5/16/16.
//  Copyright © 2016 xiao. All rights reserved.
//
#define BarWidth 30
#define XLabelWidth 40

#import "ZQBarChartView.h"

@implementation ZQBarChartView

- (id)initWithFrame:(CGRect)frame yData:(NSArray *)yValues yMax:(float)yValueMax time:(NSDate *)time
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor=[UIColor clearColor];
        _yValues=[NSMutableArray arrayWithArray:yValues];
        _yValueMax=yValueMax;
        _time = time;
        
//        _chartLine = [CAShapeLayer layer];
//        _chartLine.lineCap = kCALineCapRound;
//        _chartLine.lineJoin = kCALineJoinBevel;
//        _chartLine.fillColor   = [[UIColor whiteColor] CGColor];
//        _chartLine.lineWidth   = 3.0;
//        _chartLine.strokeEnd   = 0.0;
//        [self.layer addSublayer:_chartLine];
        
        [self initialXLabels];
    }
    return self;
}

- (id)initWithFrame:(CGRect)frame yData:(NSArray *)yValues yMax:(float)yValueMax yDic:(NSDictionary *)yDic yLevels:(NSArray *)yLevels xLabels:(NSArray *)xLabels
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor=[UIColor clearColor];
        _yValues=[NSMutableArray arrayWithArray:yValues];
        _yDic = [NSMutableDictionary dictionaryWithDictionary:yDic];
        _yLevels = [NSMutableArray arrayWithArray:yLevels];
        _xLabels = [NSMutableArray arrayWithArray:xLabels];
        _yValueMax=yValueMax;
        
        [self initialXLabels];
    }
    return self;
}

- (void)initialXLabels
{
    _time = [NSDate date];//
    //    for (UIView *view in self.subviews) {
    //        [view removeFromSuperview];
    //    }
    NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
    NSInteger x_Total = 30;
    NSInteger dateRange_x = 0;
    //    if (1) {
    [formatter setDateFormat:@"MM-dd"];
    x_Total = 30;
    dateRange_x = 1;
    self.pageCount = 4;
    //    }
    self.contentSize=CGSizeMake(self.bounds.size.width*self.pageCount+XLabelWidth, 0);
    if (self.xLabels.count>0) {
        x_Total = self.xLabels.count;
    }
    for (int i=0;i<x_Total;i++)
    {
        //4=44-80/2  20=60-80/2
        UILabel *label=[[UILabel alloc]initWithFrame:CGRectMake((i+1)*(self.frame.size.width*self.pageCount/x_Total)-XLabelWidth/2, self.frame.size.height-30, XLabelWidth, 30)];
        label.textAlignment=NSTextAlignmentCenter;
        label.font=[UIFont systemFontOfSize:13];
        //24,31,365
        label.backgroundColor=[UIColor clearColor];
        label.textColor = [UIColor whiteColor];
        
        
        if (self.xLabels) {
            NSString *timeStr = [self.xLabels objectAtIndex:i];
            NSString *str1 = [timeStr substringWithRange:NSMakeRange(5, 5)];
            if ([str1 hasPrefix:@"0"]) {
                str1 = [str1 substringWithRange:NSMakeRange(1, 4)];
            }
            label.text = str1;
        }else{
            NSDate *date2=[_time dateByAddingTimeInterval:-24*3600*i];
            NSString *datelbl= [formatter stringFromDate:date2];
            label.text=datelbl;
        }
        //[_xLabels objectAtIndex:_xLabels.count/5*i];
        [self addSubview:label];
    }
}

/*
- (void)initialXLabels
{
    _time = [NSDate date];//
//    for (UIView *view in self.subviews) {
//        [view removeFromSuperview];
//    }
    NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
    NSInteger x_Total = 30;
    NSInteger dateRange_x = 0;
//    if (1) {
        [formatter setDateFormat:@"MM-dd"];
        x_Total = 30;
        dateRange_x = 1;
        self.pageCount = 4;
//    }
    self.contentSize=CGSizeMake(self.bounds.size.width*self.pageCount, 0);
    
    for (int i=0;i<x_Total;i++)
    {
        //4=44-80/2  20=60-80/2
        UILabel *label=[[UILabel alloc]initWithFrame:CGRectMake((i+1)*(self.frame.size.width*self.pageCount/x_Total)-XLabelWidth/2, self.frame.size.height-30, XLabelWidth, 30)];
        label.textAlignment=NSTextAlignmentCenter;
        label.font=[UIFont systemFontOfSize:13];
        //24,31,365
        label.backgroundColor=[UIColor clearColor];
        label.textColor = [UIColor whiteColor];
        
        
        if (self.xLabels) {
            label.text = [NSString stringWithFormat:@"%d",i+1];
        }else{
            NSDate *date2=[_time dateByAddingTimeInterval:-24*3600*i];
            NSString *datelbl= [formatter stringFromDate:date2];
            label.text=datelbl;
        }
        //[_xLabels objectAtIndex:_xLabels.count/5*i];
        [self addSubview:label];
    }
}*/

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    
    //重新绘制画布
    CGContextRef context=UIGraphicsGetCurrentContext();
    CGContextSetStrokeColorWithColor(context, [UIColor lightGrayColor].CGColor);
    
    CGContextMoveToPoint(context, 0, (self.frame.size.height-32)+2);
    CGContextAddLineToPoint(context,self.frame.size.width , (self.frame.size.height-32)+2);

    CGContextStrokePath(context);

    
    //#warning draw standard lines

#pragma -mark 贝赛尔曲线
    
    NSInteger index = 1;
    for (NSString * valueString in _yValues) {
        // NSInteger *posiN=[_yValues indexOfObject:valueString];
        float value = [valueString floatValue];
        float grade = (float)value / (float)_yValueMax;
        
        if (index != 0) {
            CAShapeLayer *chartLine = [CAShapeLayer layer];
            chartLine.lineCap = kCALineCapRound;
            chartLine.lineJoin = kCALineJoinBevel;
            chartLine.fillColor   = [[UIColor colorWithRed:179.0/255 green:193.0/255 blue:211.0/255 alpha:0.7] CGColor];
            chartLine.lineWidth   = 3.0;
            chartLine.strokeEnd   = 0.0;
            [self.layer addSublayer:chartLine];
            
            UIBezierPath *rectPath = [UIBezierPath bezierPathWithRect:CGRectMake(index *self.frame.size.width*self.pageCount/self.xLabels.count-BarWidth/2, self.frame.size.height-30, BarWidth, -grade*(self.frame.size.height-32)-2)];

            [[UIColor whiteColor]setFill];
            chartLine.path = rectPath.CGPath;
//            [rectPath fill];
            
            CAShapeLayer *capLine = [CAShapeLayer layer];
            capLine.lineCap = kCALineCapRound;
            capLine.lineJoin = kCALineJoinBevel;
            
            capLine.fillColor   = [DefaultsHandler getColorOrderLevel:[self.yLevels objectAtIndex:index-1]].CGColor;//[[self returnColorOrderLevel:[[self.yLevels objectAtIndex:index-1]integerValue]] CGColor];
            capLine.lineWidth   = 3.0;
            capLine.strokeEnd   = 0.0;
            [self.layer addSublayer:capLine];
            
            UIBezierPath *capPath = [UIBezierPath bezierPathWithRect:CGRectMake(index *self.frame.size.width*self.pageCount/self.xLabels.count-BarWidth/2, self.frame.size.height-30-grade*(self.frame.size.height-32), BarWidth, -2)];
            [[UIColor greenColor]setFill];
            capLine.path = capPath.CGPath;
//            [capPath fill];
            
            CABasicAnimation *pathAnimation = [CABasicAnimation animationWithKeyPath:@"strokeEnd"];
            pathAnimation.duration = 1.0;
            pathAnimation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
            pathAnimation.fromValue = [NSNumber numberWithFloat:0.0f];
            pathAnimation.toValue = [NSNumber numberWithFloat:1.0f];
            pathAnimation.autoreverses = NO;
            [chartLine addAnimation:pathAnimation forKey:@"strokeEndAnimation"];
            chartLine.strokeEnd = 1.0;
        }
        index += 1;
        if (index>=_yValues.count+1) {
            break;
        }
    }
    
//    _chartLine.path = progressline.CGPath;
//    if (_strokeColor) {
//        _chartLine.strokeColor = [_strokeColor CGColor];
//    }else{
//        _chartLine.strokeColor =[[UIColor greenColor]CGColor];
//    }
    
//    _chartLine.strokeEnd = 1.0;
    self.contentOffset = CGPointMake(XLabelWidth/2-2, 0);
}

//- (UIColor *)returnColorOrderLevel:(NSInteger)level
//{
//    UIColor *color;
//    switch (level) {
//        case 1:
//            color = [UIColor greenColor];
//            break;
//        case 2:
//            color = [UIColor yellowColor];
//            break;
//        case 3:
//            color = [UIColor orangeColor];
//            break;
//        case 4:
//            color = [UIColor redColor];
//            break;
//        default:
//            break;
//    }
//    return color;
//}

@end
