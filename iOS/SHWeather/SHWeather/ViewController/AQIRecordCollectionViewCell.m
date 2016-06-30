//
//  AQIRecordCollectionViewCell.m
//  SHWeather
//
//  Created by xiao on 5/15/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "AQIRecordCollectionViewCell.h"
#import "ZQBarChartView.h"

@interface AQIRecordCollectionViewCell ()<UIScrollViewDelegate>

@property (weak, nonatomic) IBOutlet UIView *viewContent;
@property (weak, nonatomic) IBOutlet UIView *chartViewContent;
@property (strong, nonatomic) ZQBarChartView *barChartView;

@property (nonatomic) float yValueMax;
@property(nonatomic, strong)NSMutableArray *yValues;
@property(nonatomic, strong)NSMutableDictionary *yDic;
@property(nonatomic, strong)NSMutableArray *xLabels;
@property(nonatomic, strong)NSMutableArray *yLevels;
@end

@implementation AQIRecordCollectionViewCell

- (instancetype)initWithFrame:(CGRect)frame
{
    if ([super initWithFrame:frame]) {
        NSArray *arrayOfViews = [[NSBundle mainBundle] loadNibNamed:@"AQIRecordCollectionViewCell" owner:self options:nil];
        if (arrayOfViews.count<1) {
            return nil;
        }
        if (![[arrayOfViews objectAtIndex:0]isKindOfClass:[UICollectionViewCell class]]) {
            return nil;
        }
        self = [arrayOfViews objectAtIndex:0];
        self.backgroundColor = [UIColor clearColor];
        self.viewContent.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.2];
        self.chartViewContent.clipsToBounds = YES;
    
    }
    return self;
}

- (void)initialBarChartView
{
    //
//    [self initNotReallyData];
//    self.yValueMax = random()%50+240;
    
    if (!self.barChartView) {
        self.barChartView = [[ZQBarChartView alloc]initWithFrame:self.chartViewContent.bounds yData:self.yValues yMax:self.yValueMax yDic:self.yDic yLevels:self.yLevels xLabels:self.xLabels];
        self.barChartView.delegate = self;
        self.barChartView.userInteractionEnabled = YES;
        self.barChartView.pageCount = 4;
        [self.chartViewContent addSubview:self.barChartView];
    }else
    {
        self.barChartView.yValues = self.yValues;
        self.barChartView.yDic = self.yDic;
        self.barChartView.xLabels = self.xLabels;
        self.barChartView.yLevels = self.yLevels;
        self.barChartView.yValueMax = self.yValueMax;
        [self.barChartView initialXLabels];
        [self.barChartView setNeedsDisplay];
    }
}

-(void)initialDayDataWithArray:(NSArray *)data
{
    self.yValues = [NSMutableArray array];
    self.yDic = [NSMutableDictionary dictionary];
    self.xLabels = [NSMutableArray array];
    self.yLevels = [NSMutableArray array];
    self.yValueMax = 0.0;
    for (int i=0; i<data.count; i++) {
        NSDictionary *itemDic = [data objectAtIndex:i];
        
        [self.yValues addObject:[itemDic objectForKey:@"Aqi"]];
        [self.yDic setObject:[itemDic objectForKey:@"Aqi"] forKey:[itemDic objectForKey:@"Day"]];
        [self.xLabels addObject:[itemDic objectForKey:@"Day"]];
        [self.yLevels addObject:[itemDic objectForKey:@"Level"]];
        float curValue = [[itemDic objectForKey:@"Aqi"]floatValue];
        self.yValueMax = self.yValueMax>curValue?self.yValueMax:curValue;
    }
}

- (void)uploadDataWithModel:(AQIRecordModel *)model
{
    [self initialDayDataWithArray:model.daysData];
    [self performSelector:@selector(initialBarChartView) withObject:nil afterDelay:0.1];
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
    if (scrollView.contentOffset.x<18) {
        scrollView.contentOffset = CGPointMake(40/2-2, 0);
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
