//
//  HoursChartCollectionViewCell.m
//  SHWeather
//
//  Created by xiao on 5/15/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "HoursChartCollectionViewCell.h"
#import "ZQLineChartView.h"

@interface HoursChartCollectionViewCell ()<UIScrollViewDelegate>

@property (weak, nonatomic) IBOutlet UIView *viewContent;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentView;
@property (weak, nonatomic) IBOutlet UIView *curveContentView;
@property (weak, nonatomic) IBOutlet UIView *factorsContentView;
@property (strong, nonatomic) ZQLineChartView *lineChartView;
@property (weak, nonatomic) IBOutlet UIView *scrollContentView;
@property (weak, nonatomic) IBOutlet UIView *yReferView;
@property (weak, nonatomic) IBOutlet UIView *yLabelsContentView;

@property (nonatomic) float yValueMax;
@property(nonatomic, strong)NSMutableArray *yValues;
@property(nonatomic, strong)NSMutableArray *xLabels;
@property(nonatomic, strong)NSMutableArray *yPositionArray;
@property(nonatomic, strong)NSMutableDictionary *yDic;

@property (strong, nonatomic) NSString *currentParameter;
@property (strong, nonatomic) HoursModel *model;

@end

@implementation HoursChartCollectionViewCell

- (instancetype)initWithFrame:(CGRect)frame
{
    if ([super initWithFrame:frame]) {
        NSArray *arrayOfViews = [[NSBundle mainBundle] loadNibNamed:@"HoursChartCollectionViewCell" owner:self options:nil];
        if (arrayOfViews.count<1) {
            return nil;
        }
        if (![[arrayOfViews objectAtIndex:0]isKindOfClass:[UICollectionViewCell class]]) {
            return nil;
        }
        self = [arrayOfViews objectAtIndex:0];
        self.backgroundColor = [UIColor clearColor];
        self.viewContent.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.2];
        self.scrollContentView.clipsToBounds = YES;
        
//        [self performSelector:@selector(initialFactorsContentView) withObject:nil afterDelay:0.1];
    }
    return self;
}

- (void)initialScrollContentView
{
    if (!self.lineChartView) {
        self.lineChartView = [[ZQLineChartView alloc]initWithFrame:self.scrollContentView.bounds type:day yData:self.yValues yDic:self.yDic yMax:self.yValueMax xLabels:self.xLabels];
        self.lineChartView.delegate = self;
        self.lineChartView.yPositionAry = self.yPositionArray;
        self.lineChartView.contentOffset=CGPointMake(0, 0);
        self.lineChartView.showPoint = YES;
        self.lineChartView.pageCount = 4;
        self.lineChartView.strokeColor = [UIColor whiteColor];
        [self.scrollContentView addSubview:self.lineChartView];
    }else{
        self.lineChartView.yValues = self.yValues;
        self.lineChartView.yDic = self.yDic;
        self.lineChartView.yPositionAry = self.yPositionArray;
        self.lineChartView.yValueMax = self.yValueMax;
        self.lineChartView.xLabels = self.xLabels;
        self.lineChartView.startTime = [NSDate date];
        [self.lineChartView initialXLabels];
        [self.lineChartView setNeedsDisplay];
    }
    
}

- (void)initialFactorsContentView
{
//    self.factors = @[@"PM2.5",@"PM10",@"NO2",@"O3",@"SO2",@"CO"];
    for (UIView *view in self.factorsContentView.subviews) {
        [view removeFromSuperview];
    }
    for (int i=0; i<self.model.factors.count; i++) {
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        CGFloat width = self.factorsContentView.bounds.size.width/self.model.factors.count;
        btn.titleLabel.font = [UIFont systemFontOfSize:15];
        btn.frame = CGRectMake(width*i, 0, width, self.factorsContentView.bounds.size.height);
        btn.tag = 10+i;
        [btn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [btn setTitle:[self.model.factors objectAtIndex:i] forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(factorBntClick:) forControlEvents:UIControlEventTouchUpInside];
        if (i==0) {
            btn.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
        }
        [self.factorsContentView addSubview:btn];
    }
    [self initialScrollContentView];
}

-(void)setYValueMax:(float)yValueMax
{
    _yValueMax=yValueMax;
    [self setYLabels];
}

-(void)setYLabels
{
    self.yLabelsContentView.clipsToBounds = YES;

    for (UIView *view in self.yLabelsContentView.subviews) {
        [view removeFromSuperview];
    }
    
    float level = _yValueMax /6.0;//*1.4;
    
    NSInteger index = 0;
    NSInteger num =  6;
    while (num > 0) {
        //一共6行 14.5~label.height/2
        float totalHeight = self.yReferView.frame.size.height;
        UILabel * label = [[UILabel alloc] initWithFrame:CGRectMake(0,totalHeight/6*(5-index)+5, 45, 30)];
//        NSLog(@"---index:%ld----y:",(long)index);
        label.textColor = [UIColor whiteColor];
        [label setTextAlignment:NSTextAlignmentCenter];
        label.font=[UIFont systemFontOfSize:12];
        if (_yValueMax<5)
        {
            label.text=[NSString stringWithFormat:@"%.0f",roundf(index*level)];
        }
        else
        {
            label.text = [NSString stringWithFormat:@"%.0f",roundf(level * index)];
        }
        if (index == 0) {
            label.text = @"";
        }
        label.backgroundColor=[UIColor clearColor];
        [self.yLabelsContentView addSubview:label];
        index +=1 ;
        if (index>6)
        {
            return;
        }
        num -= 1;
    }
    
}

- (void)uploadDataWithModel:(HoursModel *)model
{
    self.model = model;
    if (model.factors==nil) {
        return;
    }
    if (model.twentyHoursData.count>0) {
        self.currentParameter = [self.model.factors objectAtIndex:0];
        [self initialDayDataWithType:@"Value"];
    }
    [self performSelector:@selector(initialFactorsContentView) withObject:nil afterDelay:0.1];
}

-(void)initialDayDataWithType:(NSString *)yKey
{
    NSArray *array = [self.model.twentyHoursData objectForKey:self.currentParameter];
    self.yValues = [NSMutableArray arrayWithCapacity:10];
    self.yDic = [NSMutableDictionary dictionaryWithCapacity:10];
    self.xLabels = [NSMutableArray arrayWithCapacity:24];
    self.yPositionArray = [NSMutableArray arrayWithCapacity:24];
    self.yValueMax = 0.0;
    for (int i=0; i<array.count; i++) {
        NSDictionary *itemDic = [array objectAtIndex:i];
        if ([[itemDic objectForKey:yKey] floatValue]>=0) {
            [self.yValues addObject:[itemDic objectForKey:yKey]];
            [self.yPositionArray addObject:[NSString stringWithFormat:@"%d",i]];
        }
        [self.yDic setObject:[itemDic objectForKey:yKey] forKey:[itemDic objectForKey:@"Time"]];
        float curValue = [[itemDic objectForKey:yKey]floatValue];
        self.yValueMax = self.yValueMax>curValue?self.yValueMax:curValue;
        
        [self.xLabels addObject:[itemDic objectForKey:@"Time"]];
    }
    self.yValueMax = 1.1*self.yValueMax;
}

- (void)factorBntClick:(UIButton *)sender
{
    NSLog(@"%@",sender.titleLabel.text);
    sender.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
    for (int i=0; i<self.model.factors.count; i++) {
        if ((10+i) != sender.tag) {
            UIButton *btn = (UIButton *)[self.factorsContentView viewWithTag:10+i];
            btn.backgroundColor = [UIColor clearColor];
        }
    }
    self.currentParameter = [self.model.factors objectAtIndex:sender.tag-10];
    [self initialDayDataWithType:@"Value"];
    self.segmentView.selectedSegmentIndex = 0;
    [self initialScrollContentView];
}

- (IBAction)segmentValueChange:(id)sender {
    UISegmentedControl *segment = (UISegmentedControl *)sender;
    NSString *ySign = (segment.selectedSegmentIndex == 0)?@"Value":@"Aqi";
    [self initialDayDataWithType:ySign];
    [self initialScrollContentView];
}

#pragma -mark  scrollViewDelegate
- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    if ([scrollView isKindOfClass:[UICollectionView class]]) {
        return;
    }
    if (scrollView.contentOffset.x>0) {
        AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
        app.revealSideViewController.panInteractionsWhenClosed = PPRevealSideInteractionNone;
    }
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (scrollView.contentOffset.x<20) {
        scrollView.contentOffset = CGPointMake(80/4, 0);
    }
    if ([scrollView isKindOfClass:[UICollectionView class]]) {
        return;
    }
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    if ([scrollView isKindOfClass:[UICollectionView class]]) {
        return;
    }
    if(scrollView.contentOffset.x == 0){
        AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
        app.revealSideViewController.panInteractionsWhenClosed = PPRevealSideInteractionContentView;
    }
}

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

@end
