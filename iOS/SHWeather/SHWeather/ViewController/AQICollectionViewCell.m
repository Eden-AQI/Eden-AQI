//
//  AQICollectionViewCell.m
//  SHWeather
//
//  Created by xiao on 5/15/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "AQICollectionViewCell.h"

@interface AQICollectionViewCell()<UIScrollViewDelegate>

@property (weak, nonatomic) IBOutlet UIScrollView *weatherContentView;
@property (weak, nonatomic) IBOutlet UIImageView *actorImageView;
@property (weak, nonatomic) IBOutlet UILabel *freshLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UIButton *levelBtn;
@property (weak, nonatomic) IBOutlet UILabel *aqiValuelbl;
@property (weak, nonatomic) IBOutlet UILabel *primaryParameterlbl;
@property (weak, nonatomic) IBOutlet UILabel *primaryValuelbl;
@property (weak, nonatomic) IBOutlet UIView *dataContentView;

@property (strong, nonatomic) NSLayoutConstraint *constraint;

@property (strong, nonatomic) AQIModel *model;

@end

@implementation AQICollectionViewCell

- (instancetype)initWithFrame:(CGRect)frame
{
    if ([super initWithFrame:frame]) {
        NSArray *arrayOfViews = [[NSBundle mainBundle] loadNibNamed:@"AQICollectionViewCell" owner:self options:nil];
        if (arrayOfViews.count<1) {
            return nil;
        }
        if (![[arrayOfViews objectAtIndex:0]isKindOfClass:[UICollectionViewCell class]]) {
            return nil;
        }
        self = [arrayOfViews objectAtIndex:0];
        self.backgroundColor = [UIColor clearColor];
        self.weatherContentView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3];
//        [self performSelector:@selector(initialScrollContentView) withObject:nil afterDelay:0.2];
    }
    return self;
}

- (void)initialScrollContentViewWithData:(NSArray *)forecast
{
    for(UIView *view in self.weatherContentView.subviews){
        [view removeFromSuperview];
    }
    for (int i=0; i<forecast.count; i++) {
        NSArray *data = [self forecastItemDataConvert:[forecast objectAtIndex:i]];
        UIView *dayView = [[UIView alloc]initWithFrame:CGRectMake(i*self.weatherContentView.frame.size.width/3,0,self.weatherContentView.frame.size.width/3,self.weatherContentView.frame.size.height)];
        
        for (int j=0;j<4;j++) {
            UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, j*(dayView.bounds.size.height-10)/4, dayView.bounds.size.width, dayView.bounds.size.height/4)];
            label.backgroundColor = [UIColor clearColor];
            label.textColor = [UIColor whiteColor];
            label.textAlignment = NSTextAlignmentCenter;
            NSString *dataString = [data objectAtIndex:j];
            label.text = dataString;
            if (j == 1) {
                
                label.layer.shadowColor = [UIColor whiteColor].CGColor;
                label.layer.shadowOffset = CGSizeMake(0, 1);
                
                NSArray *strArr = [dataString componentsSeparatedByString:@"~"];
                
                if (strArr.count<2) {
                    label.frame = CGRectMake(20, (j+0.5)*(dayView.bounds.size.height-10)/4-21/2.0, dayView.bounds.size.width-40, 26);
                    
                    label.backgroundColor = [DefaultsHandler getColorOrderLevel:[self getGradeOrderlevelString:[strArr firstObject]]];
                }
                else{
                    label.frame = CGRectMake(20, (j+0.5)*(dayView.bounds.size.height-10)/4-21/2.0, (dayView.bounds.size.width-40)/2.0, 26);
                    label.backgroundColor = [DefaultsHandler getColorOrderLevel:[self getGradeOrderlevelString:[strArr objectAtIndex:0]]];
                    label.text = [NSString stringWithFormat:@"%@-",[self getSimplelevelString:[strArr objectAtIndex:0]]];
                    
                    UILabel *right_label = [[UILabel alloc]initWithFrame:CGRectMake(CGRectGetMaxX(label.frame), CGRectGetMinY(label.frame), CGRectGetWidth(label.frame), CGRectGetHeight(label.frame))];
                    right_label.backgroundColor = [DefaultsHandler getColorOrderLevel:[self getGradeOrderlevelString:[strArr objectAtIndex:1]]];
                    right_label.textAlignment = NSTextAlignmentCenter;
                    right_label.text = [NSString stringWithFormat:@"%@",[self getSimplelevelString:[strArr objectAtIndex:1]]];
                    right_label.textColor = [UIColor whiteColor];
                    [dayView addSubview:right_label];
                }
            }else if (j == 2){
                NSRange range = [dataString rangeOfString:@" "];
                NSMutableAttributedString *s = [[NSMutableAttributedString alloc]initWithString:dataString];
                [s addAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12],NSForegroundColorAttributeName:[UIColor colorWithRed:0.8 green:0.8 blue:0.8 alpha:1.0]} range:NSMakeRange(range.location+range.length, dataString.length - range.location-range.length)];
                [s addAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:18],NSForegroundColorAttributeName:[UIColor whiteColor]} range:NSMakeRange(0, range.location)];
                label.attributedText = s;
            }
            //NSLog(@"-------j:%d-----text:%@",j,[data objectAtIndex:j]);
//            NSString *text = [data objectAtIndex:j];
//            if ([text isEqual:[NSNull null]]) {
//                text = @"";
//                NSLog(@"---eee-");
//            }
            
            [dayView addSubview:label];
        }
        UIView *seprateView = [[UIView alloc]initWithFrame:CGRectMake(dayView.bounds.size.width-0.5, 0, 0.5, dayView.bounds.size.height)];
        seprateView.backgroundColor = [UIColor whiteColor];
        [dayView addSubview:seprateView];
        [self.weatherContentView addSubview:dayView];
        self.weatherContentView.delegate = self;
    }
    self.weatherContentView.contentSize = CGSizeMake(self.weatherContentView.frame.size.width/3*forecast.count, self.weatherContentView.frame.size.height);
}

- (NSString *)getGradeOrderlevelString:(NSString *)levelStr{
    NSString *grade = @"1";
    if ([levelStr containsString:@"优"]) {
        return @"1";
    }else if ([levelStr containsString:@"良"]) {
        return @"2";
    }else if ([levelStr containsString:@"轻度"]) {
        return @"3";
    }else if ([levelStr containsString:@"中度"]) {
        return @"4";
    }else if ([levelStr containsString:@"重度"]) {
        return @"5";
    }else if ([levelStr containsString:@"严重"]) {
        return @"6";
    }
    return grade;
}

- (NSString *)getSimplelevelString:(NSString *)levelStr
{
    NSString *simpleStr = @"优";
    if ([levelStr containsString:@"优"]) {
        return @"优";
    }else if ([levelStr containsString:@"良"]) {
        return @"良";
    }else if ([levelStr containsString:@"轻度"]) {
        return @"轻度";
    }else if ([levelStr containsString:@"中度"]) {
        return @"中度";
    }else if ([levelStr containsString:@"重度"]) {
        return @"重度";
    }else if ([levelStr containsString:@"严重"]) {
        return @"严重";
    }
    return simpleStr;
}

- (void)uploadDataWithModel:(AQIModel *)model
{
    self.model = model;
    NSDateFormatter *formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"HH:mm"];
    NSString *dateString = [formatter stringFromDate:[NSDate date]];
    self.freshLabel.text = [NSString stringWithFormat:@"今天%@更新",dateString];
    
    self.timeLabel.text = model.updateTime;
    [self.levelBtn setImage:model.levelImage forState:UIControlStateNormal];
    NSString *btnTitle = [[DefaultsHandler getDescriptionOrderLevel:model.aqiLevel]objectForKey:@"AQIState"];
    [self.levelBtn setTitle:btnTitle forState:UIControlStateNormal];
    self.primaryParameterlbl.text = model.primaryParameter;
    self.primaryValuelbl.text = model.PrimaryValue;
    self.aqiValuelbl.text = model.aqiValue;
    self.actorImageView.image = model.actorImage;
    UIView *ctView = self.dataContentView;
    if (model.forecast.count<1) {
        NSLog(@"---empty");
//        self.weatherContentView.hidden = YES;
        if (self.constraint == nil) {
            self.constraint = [NSLayoutConstraint constraintWithItem:ctView.superview attribute:NSLayoutAttributeBottom relatedBy:NSLayoutRelationEqual toItem:ctView attribute:NSLayoutAttributeBottom multiplier:1 constant:10];
        }
        
        [self.contentView addConstraint:self.constraint];
        [self.contentView updateConstraintsIfNeeded];
        return ;
    }else{
//        self.weatherContentView.hidden = NO;
//        UIView *weaView = self.weatherContentView;
//        NSMutableArray *constraints = [NSMutableArray array];
//        [constraints addObject:[NSLayoutConstraint constraintWithItem:weaView attribute:NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:ctView attribute:NSLayoutAttributeBottom multiplier:1 constant:0]];
//        [constraints addObject:[NSLayoutConstraint constraintWithItem:weaView.superview attribute:NSLayoutAttributeBottom relatedBy:NSLayoutRelationEqual toItem:weaView attribute:NSLayoutAttributeBottom multiplier:1 constant:0]];
//        [constraints addObjectsFromArray:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-[weaView(137)]-|" options:0 metrics:nil views:NSDictionaryOfVariableBindings(weaView)]];
//        [self.contentView addConstraints:constraints];
        [self.contentView removeConstraint:self.constraint];
        [self.contentView updateConstraintsIfNeeded];
    }
    [self initialScrollContentViewWithData:(model.forecast)];
}

- (NSArray *)forecastItemDataConvert:(NSDictionary *)itemDic
{
    NSMutableArray *muArray =[[NSMutableArray alloc]initWithCapacity:10];
    [muArray addObject:[NSString stringWithFormat:@"%@",[itemDic objectForKey:@"Time"]]];
    [muArray addObject:[NSString stringWithFormat:@"%@",[itemDic objectForKey:@"AqiLevel"]]];
    [muArray addObject:[NSString stringWithFormat:@"%@ %@",[itemDic objectForKey:@"Aqi"],[itemDic objectForKey:@"PrimaryParameter"]]];
    NSString *temperature = [itemDic objectForKey:@"Temperature"];
    if ([temperature isEqual:[NSNull null]]) {
        temperature = @"";
    }
    [muArray addObject:[NSString stringWithFormat:@"%@",temperature]];
    return muArray;
}

- (IBAction)levelBtnAction:(id)sender {
    NSLog(@"action");
    if (self.delegate && [self.delegate respondsToSelector:@selector(detailBtnAction)]) {
        [self.delegate detailBtnAction];
    }
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
