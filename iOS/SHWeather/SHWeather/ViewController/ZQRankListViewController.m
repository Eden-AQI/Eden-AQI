//
//  ZQRankListViewController.m
//  SHWeather
//
//  Created by xiao on 5/14/16.
//  Copyright © 2016 xiao. All rights reserved.
//
//typedef NS_ENUM(NSInteger, DataType)
//{
//    ShiShi = 0,
//    RiBao,
//    YueBao,
//    NianBao
//};

#import "ZQRankListViewController.h"
#import "RankTableViewCell.h"
#import "MJRefresh.h"

@interface ZQRankListViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (weak, nonatomic) IBOutlet UIView *parametersContentView;
@property (weak, nonatomic) IBOutlet UITableView *rankTableView;

@property (strong, nonatomic) UIScrollView *scrollContentView;
@property (strong, nonatomic) UIView *cursorView;

@property (strong, nonatomic) NSArray *rankArray;
@property (strong, nonatomic) NSArray *parametersArray;
//
@property (nonatomic) NSInteger currentDataType;
@property (strong, nonatomic) NSString *currentParameter;
@property (strong, nonatomic) NSMutableArray *currentRankArray;

@property (nonatomic) BOOL viewWillFirstAppear;

@end

@implementation ZQRankListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.currentDataType = 0;
    self.currentParameter = @"AQI";
    // 设置header
    MJRefreshNormalHeader *tableViewHeader = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(getRankingData)];
    
    // 设置自动切换透明度(在导航栏下面自动隐藏)
    tableViewHeader.automaticallyChangeAlpha = YES;
//    tableViewHeader.stateLabel.textColor = [UIColor whiteColor];
    //    header.arrowView.alpha = 0;
//    tableViewHeader.activityIndicatorViewStyle = UIActivityIndicatorViewStyleWhite;
    // 隐藏时间
    tableViewHeader.lastUpdatedTimeLabel.hidden = YES;
    self.rankTableView.mj_header = tableViewHeader;

    // Do any additional setup after loading the view from its nib.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
    app.revealSideViewController.panInteractionsWhenClosed =PPRevealSideInteractionNone;
    
    [self getRankingData];
}

- (void)getRankingData
{
    NSString *api = [Global api:@"Aqi/GetRankingData"];
    AFHTTPRequestOperationManager *request = [Global createRequest];
    [request GET:api parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        self.rankArray = (NSArray *)responseObject;
        [self reloadData];
        [self.rankTableView.mj_header endRefreshing];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [[[UIAlertView alloc] initWithTitle:@"提示" message:@"数据加载失败" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles: nil] show];
        self.rankArray = nil;
        [self reloadData];
        [self.rankTableView.mj_header endRefreshing];
    }];
    
}

- (void)reloadData
{
    [self reassemblyData];
    [self.rankTableView reloadData];
}

- (void)reassemblyData
{
    self.currentRankArray = [NSMutableArray arrayWithCapacity:10];
    NSDictionary *durationDic = [self.rankArray objectAtIndex:self.currentDataType];
    NSArray *parameterAry = [durationDic objectForKey:@"Items"];
    for (NSDictionary *parameterDic in parameterAry) {
        if ([[parameterDic objectForKey:@"Parameter"] isEqualToString:self.currentParameter]) {
            self.currentRankArray = (NSMutableArray *)[parameterDic objectForKey:@"Data"];
            
            NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"Value" ascending:YES];//其中，price为数组中的对象的属性，这个针对数组中存放对象比较更简洁方便
            NSArray *sortDescriptors = [[NSArray alloc] initWithObjects:&sortDescriptor count:1];
            [self.currentRankArray sortUsingDescriptors:sortDescriptors];
            break;
        }
    }
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    if (self.viewWillFirstAppear == NO) {
        [self initialHeaderView];
        [self initialParametersContentView];
    }
    self.viewWillFirstAppear = YES;
    
}

- (void)initialHeaderView
{
    NSArray *segmentTitles = @[@"实时",@"日报",@"月报",@"年报"];
    float headerWidth = self.headerView.frame.size.width;
    for (int i=0; i<4; i++) {
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.frame = CGRectMake(((headerWidth-50)-80*4)/5.0+25+(80+((headerWidth-50)-80*4)/5.0)*i, 24, 80, 30);
        [btn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        btn.titleLabel.font = [UIFont systemFontOfSize:20];
        [btn setTitle:[segmentTitles objectAtIndex:i] forState:UIControlStateNormal];
        btn.backgroundColor = [UIColor clearColor];
        btn.tag = 10+i;
        [btn addTarget:self action:@selector(segmentButtonClick:) forControlEvents:UIControlEventTouchUpInside];
        UIView *cursorView = [[UIView alloc]initWithFrame:CGRectMake(btn.frame.origin.x+15, btn.frame.origin.y+btn.frame.size.height, btn.frame.size.width-30, 2)];
        if (i == 0) {
            cursorView.backgroundColor = [UIColor whiteColor];
        }
        cursorView.tag = 20+i;
        [self.headerView addSubview:cursorView];
        [self.headerView addSubview:btn];
    }
}

- (void)initialParametersContentView
{
    self.parametersArray = @[@"AQI",@"PM2.5",@"SO2",@"NO2",@"O3",@"CO",@"PM10"];
    float contentWidth = self.parametersContentView.frame.size.width;
    float contentHeight = self.parametersContentView.frame.size.height;
    self.scrollContentView = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, contentWidth, contentHeight-2)];
    self.scrollContentView.contentSize = CGSizeMake(contentWidth/5.0*self.parametersArray.count, contentHeight);
    self.scrollContentView.showsVerticalScrollIndicator = NO;
    self.scrollContentView.showsHorizontalScrollIndicator = NO;
    self.scrollContentView.scrollEnabled = NO;
    
    for (int i=0; i<self.parametersArray.count; i++) {
        UIButton *imageBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        imageBtn.frame = CGRectMake(contentWidth/5.0*i, 0, contentWidth/5.0, self.scrollContentView.frame.size.height);
        [imageBtn setImage:[UIImage imageNamed:[self.parametersArray objectAtIndex:i]] forState:UIControlStateNormal];
        imageBtn.tag = 100+i;
        [imageBtn addTarget:self action:@selector(parametersButtonClick:) forControlEvents:UIControlEventTouchUpInside];
        [self.scrollContentView addSubview:imageBtn];
        if (i<self.parametersArray.count-1) {
            UIView *separateView = [[UIView alloc]initWithFrame:CGRectMake(CGRectGetMaxX(imageBtn.frame), 10, 0.5, CGRectGetHeight(imageBtn.frame)-20)];
            separateView.backgroundColor = [UIColor lightGrayColor];
            [self.scrollContentView addSubview:separateView];
        }
    }
    
    self.cursorView = [[UIView alloc]initWithFrame:CGRectMake(10, contentHeight-2, contentWidth/5.0-20, 2)];
    self.cursorView.backgroundColor = [UIColor blueColor];
    [self.parametersContentView addSubview:self.cursorView];
    [self.parametersContentView addSubview:self.scrollContentView];
}

- (void)segmentButtonClick:(UIButton *)segmentBtn
{
    for (UIView *view in self.headerView.subviews) {
        if (view.tag == segmentBtn.tag+10) {
            view.backgroundColor = [UIColor whiteColor];
        }else{
            view.backgroundColor = [UIColor clearColor];
        }
    }
    self.currentDataType = segmentBtn.tag - 10;
    [self reloadData];
    NSLog(@"click index:%ld",segmentBtn.tag-10);
}

- (void)parametersButtonClick:(UIButton *)parameterBtn
{
    NSInteger index = parameterBtn.tag - 100;
    
    CGRect leftMoveRect = CGRectMake(0, 0, parameterBtn.bounds.size.width*2, parameterBtn.bounds.size.height);
    CGRect rightMoveRect = CGRectMake(3*parameterBtn.bounds.size.width, 0, parameterBtn.bounds.size.width*2, parameterBtn.bounds.size.height);
    BOOL leftBtn = (index == 1 || index == 2 || index == 3)?YES:NO;
    BOOL rightBtn = (index == 3 || index == 4 || index == 5)?YES:NO;
    CGRect btnRect = [parameterBtn.superview convertRect:parameterBtn.frame toView:self.parametersContentView];
    BOOL leftMoved = NO;
    BOOL rightMoved = NO;
    if (leftBtn && CGRectContainsRect(leftMoveRect, btnRect) &&self.scrollContentView.contentOffset.x>0) {
        [UIView animateWithDuration:0.2 animations:^{
            self.scrollContentView.contentOffset = CGPointMake(self.scrollContentView.contentOffset.x-parameterBtn.frame.size.width, 0);
        }];
        leftMoved = YES;
    }else if (rightBtn && CGRectContainsRect(rightMoveRect, btnRect) &&self.scrollContentView.contentOffset.x<parameterBtn.frame.size.width*2) {
        [UIView animateWithDuration:0.2 animations:^{
            self.scrollContentView.contentOffset = CGPointMake(self.scrollContentView.contentOffset.x+parameterBtn.frame.size.width, 0);
        }];
        rightMoved = YES;
    }
    float moveOrigin = 0;
    
    if (leftMoved) {
        moveOrigin = parameterBtn.frame.size.width;
    }else if (rightMoved){
        moveOrigin = -parameterBtn.frame.size.width;
    }
    [UIView animateWithDuration:0.2 animations:^{
        self.cursorView.center = CGPointMake(btnRect.origin.x +moveOrigin + btnRect.size.width*0.5, self.cursorView.center.y);
    }];
    
    self.currentParameter = [self.parametersArray objectAtIndex:index];
    [self reloadData];
    NSLog(@" parameterBtn click index:%ld",parameterBtn.tag-100);
}

#pragma -mark UITableViewDelegate,UITableViewDataSource
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 60;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 15;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return self.currentRankArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *indentifierCell = @"Cell";
    RankTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:indentifierCell];
    if (!cell) {
        NSArray *nibs=[[NSBundle mainBundle]loadNibNamed:@"RankTableViewCell" owner:nil options:nil];
        if (nibs.count>0) {
            for (id view in nibs) {
                if ([view isMemberOfClass:[RankTableViewCell class]]) {
                    cell=(RankTableViewCell *)view;
                }
            }
        }

    }
    NSDictionary *dataDic = [self.currentRankArray objectAtIndex:indexPath.section];
    cell.citylbl.text = [dataDic objectForKey:@"SiteName"];
    NSString *valueText = [NSString stringWithFormat:@"%@",[dataDic objectForKey:@"Value"]];
    float value = [[dataDic objectForKey:@"Value"] floatValue];
    if (value<1.0) {
        valueText = [NSString stringWithFormat:@"%.2f",value];
    }
    cell.AQIlbl.text = valueText;
    cell.AQIlbl.textColor = [DefaultsHandler getColorOrderAQIValue:[dataDic objectForKey:@"Value"]];
//    if (indexPath.section>0) {
//        cell.rankImageView.hidden = YES;
//    }
//    cell.ranklbl.text = [NSString stringWithFormat:@"%ld",indexPath.section+1];
//    if (indexPath.section<3) {
//        cell.ranklbl.textColor = [UIColor orangeColor];
//    }else{
//        cell.ranklbl.textColor = [UIColor colorWithRed:74.0/255 green:130.0/255 blue:239.0/255 alpha:1];
//    }
    return cell;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
