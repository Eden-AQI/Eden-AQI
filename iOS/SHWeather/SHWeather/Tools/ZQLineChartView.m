//
//  ZQLineChartView.m
//  SHWeather
//
//  Created by xiao on 5/16/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#define XLabelWidth 80

#import "ZQLineChartView.h"
#import "NSDate+Common.h"

@implementation ZQLineChartView

- (id)initWithFrame:(CGRect)frame type:(int)time_type yData:(NSArray *)yValues yDic:(NSDictionary *)yDic yMax:(float)yValueMax xLabels:(NSArray *)xLabels
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor=[UIColor clearColor];
        _yValues=[NSMutableArray arrayWithArray:yValues];
        _yDic=[NSMutableDictionary dictionaryWithDictionary:yDic];
        
        _time_type=(returnTime_type)time_type;

        _yValueMax=yValueMax;
        _xLabels=xLabels;
        
        _chartLine = [CAShapeLayer layer];
        _chartLine.lineCap = kCALineCapRound;
        _chartLine.lineJoin = kCALineJoinBevel;
        _chartLine.fillColor   = [[UIColor whiteColor] CGColor];
        _chartLine.lineWidth   = 1.0;
        _chartLine.strokeEnd   = 0.0;
        [self.layer addSublayer:_chartLine];
        
        [self initialXLabels];
    }
    return self;
}

- (void)initialXLabels
{
    _startTime = [NSDate date];
    for (UIView *view in self.subviews) {
        [view removeFromSuperview];
    }
    NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
    NSInteger x_Range = 1;
    NSInteger x_Total = 24;
    NSInteger dateRange_x = 0;
    if (_time_type == day) {
        [formatter setDateFormat:@"HH:mm"];
        x_Range = 24;
        x_Total = 24;
        dateRange_x = 1;
        self.pageCount = 4;
//        self.yPositionAry = self.xLabels;
    }
    self.contentSize=CGSizeMake(self.bounds.size.width*self.pageCount+XLabelWidth/2, 0);
    NSMutableArray *muArray = [NSMutableArray arrayWithCapacity:10];
    for (int i=0;i<x_Total;i++)
    {
        //4=44-80/2  20=60-80/2
        UILabel *label=[[UILabel alloc]initWithFrame:CGRectMake((i+1)*(self.frame.size.width*self.pageCount/x_Total)-XLabelWidth/2, self.frame.size.height-30, XLabelWidth, 30)];
        label.textAlignment=NSTextAlignmentCenter;
        label.font=[UIFont systemFontOfSize:14];
        //24,31,365
        label.backgroundColor=[UIColor clearColor];
        label.textColor = [UIColor whiteColor];
        if (self.xLabels && self.xLabels.count < i) {
            NSString *timeStr = [self.xLabels objectAtIndex:i];
            NSArray *timeAry = [timeStr componentsSeparatedByString:@" "];
            //NSString *timeStr = i<10?[NSString stringWithFormat:@"0%d:00",i]:[NSString stringWithFormat:@"%d:00",i];
            label.text = [timeAry lastObject];;
        }else{
            NSDate *date2=[_startTime dateByAddingTimeInterval:3600*i];
            NSString *datelbl= [formatter stringFromDate:date2];
            label.text=datelbl;
        }
        [muArray addObject:[NSString stringWithFormat:@"%d",i]];
        //[_xLabels objectAtIndex:_xLabels.count/5*i];
        [self addSubview:label];
    }
    self.yPositionAry = muArray;
}

- (id)initWithFrame:(CGRect)frame Color:(UIColor *)strokeColor type:(int)time_type standard:(NSDictionary *)standardValues ydata:(NSArray *)yValues yMax:(float)yValueMax xlabels:(NSArray *)xlabels yDic:(NSDictionary *)yDic startTime:(NSDate *)startTime yIndexAccor:(NSDictionary *)yIndexDic
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor=[UIColor clearColor];
        _yValues=[NSMutableArray array];
        _yDic=[NSMutableDictionary dictionary];
        
        _strokeColor=strokeColor;
        _time_type=(returnTime_type)time_type;
        _standardLines=standardValues;
        
        _yValueMax=yValueMax;
        _xLabels=xlabels;
        
        _startTime=startTime;
        _yIndexDic=yIndexDic;
        
        //  [self sorted];
        _chartLine = [CAShapeLayer layer];
        _chartLine.lineCap = kCALineCapRound;
        _chartLine.lineJoin = kCALineJoinBevel;
        _chartLine.fillColor   = [[UIColor whiteColor] CGColor];
        _chartLine.lineWidth   = 1.0;
        _chartLine.strokeEnd   = 0.0;
        [self.layer addSublayer:_chartLine];
        self.backgroundColor = [UIColor yellowColor];
        if (_time_type==year)
        {
            UIView *seaprad=[[UIView alloc]initWithFrame:CGRectMake(0, self.frame.size.height-30, self.frame.size.width*2, 0.5)];
            seaprad.backgroundColor=[UIColor blackColor];
            [self addSubview:seaprad];

            NSDateFormatter *formatter=[[NSDateFormatter alloc]init];
            [formatter setDateFormat:@"HH-MM"];
            
            for (int i=0;i<18;i++)
            {
                //4=44-80/2  20=60-80/2
                UILabel *label=[[UILabel alloc]initWithFrame:CGRectMake(i*(self.frame.size.width*2/365.0*21+20)-40, self.frame.size.height-30, 80, 30)];
                label.textAlignment=NSTextAlignmentCenter;
                //label.backgroundColor=[UIColor redColor];
                label.font=[UIFont systemFontOfSize:14];
                //24,31,365
                label.backgroundColor=[UIColor clearColor];
                NSDate *date2=[_startTime dateByAddingTimeInterval:24*3600*27*i];
                NSString *datelbl=[formatter stringFromDate:date2];
                label.text=datelbl;
                
                if (i==0)
                {
                    label.alpha=0;
                }
                //[_xLabels objectAtIndex:_xLabels.count/5*i];
                [self addSubview:label];
            }
            
        }
    }
    return self;
}

-(void)setStrokeColor:(UIColor *)strokeColor
{
    _strokeColor=strokeColor;
    _chartLine.strokeColor=[strokeColor CGColor];
}
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    NSLog(@"drawRect----++++");
    //重新绘制画布
    CGContextRef context=UIGraphicsGetCurrentContext();
    CGContextSetStrokeColorWithColor(context, [UIColor lightGrayColor].CGColor);
    
    if (self.needGrid) {
        if (_time_type==day||_time_type==recently24h)
        {
            for (int i=1; i<=24; i++)
            {
                CGContextMoveToPoint(context, i*self.frame.size.width/24, 0);
                CGContextAddLineToPoint(context,i*self.frame.size.width/24 , self.frame.size.height);
            }
        }
        else if (_time_type==recently15d)
        {
            for (int i=1; i<=15; i++)
            {
                CGContextMoveToPoint(context, i*self.frame.size.width/15, 0);
                CGContextAddLineToPoint(context,i*self.frame.size.width/15 , self.frame.size.height);
            }
        }
        else if (_time_type==month)
        {
            for (int i=1; i<=31; i++)
            {
                CGContextMoveToPoint(context,i*self.frame.size.width/31, 0);
                CGContextAddLineToPoint(context,i*self.frame.size.width/31 , self.frame.size.height);
            }
        }
        else if (_time_type==year)
        {
            for (int i=1; i<=365; i++)
            {
                CGContextMoveToPoint(context, i*self.frame.size.width/365.0*2, 0);
                CGContextAddLineToPoint(context,i*self.frame.size.width/365.0*2 ,self.frame.size.height-30);
            }
            
        }
        else
        {
            for (int i=0; i<_xLabels.count; i++)
            {
                CGContextMoveToPoint(context, i*self.frame.size.width/_xLabels.count, 0);
                CGContextAddLineToPoint(context, i*self.frame.size.width/_xLabels.count, self.frame.size.height);
            }
        }
        CGContextStrokePath(context);
    }
    
    //#warning draw standard lines
    for (int i=0; i<_standardLines.count; i++)
    {
        NSArray *ary=[_standardLines allKeys];//颜色
        NSString *color=[ary objectAtIndex:i];
        float rate=[[_standardLines objectForKey:color] floatValue];
        float standardGrade=rate/(float)_yValueMax;
        NSArray *colorAry=[color componentsSeparatedByString:@","];
        //    NSLog(@"---colorAry-%@",colorAry);
        
        CGContextSetStrokeColorWithColor(context, [[UIColor colorWithRed:[colorAry[0]floatValue] green:[colorAry[1]floatValue] blue:[colorAry[2]floatValue] alpha:1.0]CGColor]);
        
        NSArray *orangeAry=@[@"255",@"126",@"0"];
        if ([colorAry isEqualToArray:orangeAry]) {
            CGContextSetStrokeColorWithColor(context, [[UIColor orangeColor]CGColor]);
        }
        
        if (_time_type==year)
        {
            CGContextMoveToPoint(context, 0, (1-standardGrade)*(self.frame.size.height-30));
            CGContextAddLineToPoint(context, self.frame.size.width, (1-standardGrade)*(self.frame.size.height-30));
        }
        else
        {
            CGContextMoveToPoint(context, 0, (1-standardGrade)*self.frame.size.height);
            CGContextAddLineToPoint(context, self.frame.size.width, (1-standardGrade)*self.frame.size.height);
        }
        CGContextStrokePath(context);
    }
    
    if (_yValues==nil||_yValues.count<1) {
        _chartLine.hidden = YES;
        return;
    }else{
        _chartLine.hidden = NO;
    }
    
#pragma -mark 贝赛尔曲线  其他
    UIBezierPath *progressline = [UIBezierPath bezierPath];

    CGFloat firstValue = [[_yValues objectAtIndex:0] floatValue];
    int firstPosi=[[_yPositionAry objectAtIndex:0] intValue];
    //0<->firstPosi*self.frame.size.width/(float)_yValues.count
    float grade = (float)firstValue / (float)_yValueMax;
    
    if (_time_type==year)
    {
        [progressline moveToPoint:CGPointMake(firstPosi*self.frame.size.width/365*2, (1-grade)*(self.frame.size.height-30))];
    }
    else if(_time_type==day)
    {
        [progressline moveToPoint:CGPointMake((firstPosi+1)*self.frame.size.width*self.pageCount/24, (1-grade)*(self.frame.size.height-30))];//firstPosi
        if (self.showPoint) {
            // 添加圆到path
            [progressline addArcWithCenter:CGPointMake((firstPosi+1)*self.frame.size.width*self.pageCount/24, (1-grade)*(self.frame.size.height-30)) radius:3.0 startAngle:0.0 endAngle:M_PI*2 clockwise:YES];
        }
    }
    else if(_time_type==month)
    {
        [progressline moveToPoint:CGPointMake(firstPosi*self.frame.size.width/31, (1-grade)*self.frame.size.height)];
    }
    else
    {
        [progressline moveToPoint:CGPointMake(0, (1-grade)*self.frame.size.height)];
    }
    
    
    [progressline setLineWidth:0.2];
    [progressline setLineCapStyle:kCGLineCapRound];
    [progressline setLineJoinStyle:kCGLineJoinRound];
    
    
#pragma -mark index换成shoot
    NSInteger index = 0;
    for (NSString * valueString in _yValues) {
        // NSInteger *posiN=[_yValues indexOfObject:valueString];
        float value = [valueString floatValue];
        float grade = (float)value / (float)_yValueMax;
        NSInteger shoot=[[_yPositionAry objectAtIndex:index] intValue];
        //xLabel_Width+xlabel_Padding=99
        if (shoot != 0) {
            if (_time_type==year)
            {
                [progressline addLineToPoint:CGPointMake((index *self.frame.size.width/365)*2,(1-grade)*(self.frame.size.height-30))];
                
                [progressline moveToPoint:CGPointMake((index *self.frame.size.width/365)*2,(1-grade)*(self.frame.size.height-30))];
                
            }
            else if (_time_type==day)
            {
                [progressline addLineToPoint:CGPointMake(((shoot+1) *self.frame.size.width*self.pageCount/24),(1-grade)*(self.frame.size.height-30))];//shoot
                
                [progressline moveToPoint:CGPointMake(((shoot+1) *self.frame.size.width*self.pageCount/24),(1-grade)*(self.frame.size.height-30))];//shoot
                if (self.showPoint) {
                    // 添加圆到path
                    [progressline addArcWithCenter:CGPointMake(((shoot+1) *self.frame.size.width*self.pageCount/24), (1-grade)*(self.frame.size.height-30)) radius:3.0 startAngle:0.0 endAngle:M_PI*2 clockwise:YES];
                }
                
            }
            else if (_time_type==month)
            {
                [progressline addLineToPoint:CGPointMake((shoot *self.frame.size.width/31),(1-grade)*self.frame.size.height)];
                
                [progressline moveToPoint:CGPointMake((shoot *self.frame.size.width/31),(1-grade)*self.frame.size.height)];
            }
            else
            {
                [progressline addLineToPoint:CGPointMake((index *self.frame.size.width/_xLabels.count),(1-grade)*self.frame.size.height)];
                
                [progressline moveToPoint:CGPointMake((index *self.frame.size.width/_xLabels.count),(1-grade)*self.frame.size.height)];
            }
            
            
            // [progressline stroke];
        }
        index += 1;
        if (index>=_yValues.count) {
            break;
        }
    }
    
    _chartLine.path = progressline.CGPath;
    if (_strokeColor) {
        _chartLine.strokeColor = [_strokeColor CGColor];
    }else{
        _chartLine.strokeColor =[[UIColor greenColor]CGColor];
    }
    
    
//    CABasicAnimation *pathAnimation = [CABasicAnimation animationWithKeyPath:@"strokeEnd"];
//    pathAnimation.duration = 1.0;
//    pathAnimation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
//    pathAnimation.fromValue = [NSNumber numberWithFloat:0.0f];
//    pathAnimation.toValue = [NSNumber numberWithFloat:1.0f];
//    pathAnimation.autoreverses = NO;
//    [_chartLine addAnimation:pathAnimation forKey:@"strokeEndAnimation"];
    
    _chartLine.strokeEnd = 1.0;
    
    self.contentOffset = CGPointMake(XLabelWidth/4, 0);
}

@end
