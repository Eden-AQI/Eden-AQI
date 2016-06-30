//
//  DetailCollectionViewCell.m
//  SHWeather
//
//  Created by xiao on 5/15/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "DetailCollectionViewCell.h"

@interface DetailCollectionViewCell ()

@property (weak, nonatomic) IBOutlet UIView *chieflyContentView;
@property (weak, nonatomic) IBOutlet UITableView *factorTableView;
@property (weak, nonatomic) IBOutlet UILabel *healthImpactlbl;
@property (weak, nonatomic) IBOutlet UILabel *suggestlbl;
@property (weak, nonatomic) IBOutlet UILabel *primaryValuelbl;
@property (weak, nonatomic) IBOutlet UILabel *primaryParameterlbl;

@property (strong, nonatomic) UIColor *drawColor;

@property (strong, nonatomic) NSMutableDictionary *data;

@end

@implementation DetailCollectionViewCell

- (instancetype)initWithFrame:(CGRect)frame
{
    if ([super initWithFrame:frame]) {
        NSArray *arrayOfViews = [[NSBundle mainBundle] loadNibNamed:@"DetailCollectionViewCell" owner:self options:nil];
        if (arrayOfViews.count<1) {
            return nil;
        }
        if (![[arrayOfViews objectAtIndex:0]isKindOfClass:[UICollectionViewCell class]]) {
            return nil;
        }
        self = [arrayOfViews objectAtIndex:0];
        self.backgroundColor = [UIColor clearColor];
        
        self.data = [NSMutableDictionary dictionary];
        [self.data setValue:@"75ug/m3" forKey:@"PM2.5"];
        [self.data setValue:@"128ug/m3" forKey:@"PM10"];
        [self.data setValue:@"65ug/m3" forKey:@"NO2"];
        [self.data setValue:@"31ug/m3" forKey:@"O3"];
        [self.data setValue:@"21ug/m3" forKey:@"SO2"];
        [self.data setValue:@"1.5ug/m3" forKey:@"CO"];
        
        self.factorTableView.delegate = self;
        self.factorTableView.dataSource = self;
        self.factorTableView.scrollEnabled = NO;
        self.factorTableView.backgroundColor = [UIColor clearColor];
        self.factorTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        
        self.chieflyContentView.backgroundColor = [UIColor clearColor];
        //[self drawChieflyView];
        [self performSelector:@selector(drawChieflyView) withObject:nil afterDelay:0.1];
    }
    return self;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)drawChieflyView
{
    self.chieflyContentView.layer.cornerRadius = self.chieflyContentView.frame.size.width/2;
    self.chieflyContentView.clipsToBounds = YES;
    
    float number = 102;
    if (number>100) {
        self.chieflyContentView.layer.borderColor = self.drawColor.CGColor;
        self.chieflyContentView.layer.borderWidth = 4.0;
    }
}

#pragma -mark UITableViewDelegate,UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.data.allKeys.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *indentifierCell = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:indentifierCell];
    if (!cell) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:indentifierCell];
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.textLabel.text = [self.data.allKeys objectAtIndex:indexPath.row];
    cell.detailTextLabel.text = [self.data objectForKey:cell.textLabel.text];
    cell.textLabel.textColor = [UIColor whiteColor];
    cell.detailTextLabel.textColor = [UIColor whiteColor];
    cell.detailTextLabel.backgroundColor = [UIColor clearColor];
    cell.textLabel.backgroundColor = [UIColor clearColor];
    cell.contentView.backgroundColor = [UIColor clearColor];
    cell.backgroundColor = [UIColor clearColor];
    return cell;
}

- (void)uploadDataWithModel:(AQIDetailModel *)model
{
    self.primaryValuelbl.text = model.PrimaryValue;
    self.drawColor = [DefaultsHandler getColorOrderLevel:model.aqiLevel];
    self.primaryValuelbl.textColor = self.drawColor;
    self.primaryParameterlbl.text = [NSString stringWithFormat:@"首要污染物：%@",model.primaryParameter];
    self.data = [NSMutableDictionary dictionaryWithDictionary:model.otherParameters];
    [self.factorTableView reloadData];
    self.healthImpactlbl.text = model.health;
    self.suggestlbl.text = model.suggest;
}

@end
