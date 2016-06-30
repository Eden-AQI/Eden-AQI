//
//  SiteManagerViewController.m
//  SHWeather
//
//  Created by xiao on 6/1/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "SiteTableViewCell.h"
#import "SiteManagerViewController.h"
#import "SiteOptionsViewController.h"

@interface SiteManagerViewController ()<UITableViewDelegate,UITableViewDataSource,SiteOptionsViewControllerDelegate>

@property (weak, nonatomic) IBOutlet UIButton *editBtn;
@property (weak, nonatomic) IBOutlet UITableView *siteTableView;
@property (strong, nonatomic) NSMutableArray *followCities;

@property (strong, nonatomic) NSDictionary *citiesDataDic;
@property (strong, nonatomic) NSArray *dataArray;

@end

@implementation SiteManagerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor yellowColor];
    self.view.backgroundColor = [UIColor clearColor];
    
    self.siteTableView.userInteractionEnabled = YES;
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(locationWithNotification:) name:LOCATION_NOTI object:nil];
//    self.siteTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    //    self.view.frame = CGRectMake(0, 0, self.view.bounds.size.width-80, self.view.bounds.size.height);
    // Do any additional setup after loading the view from its nib.
}

- (void)locationWithNotification:(NSNotification *)noti
{
//    [DefaultsHandler editPairWithData:@"NO" withKey:LOCATIONCHANGED];
    SiteModel *site = (SiteModel *)noti.object;
    [DefaultsHandler editPairWithData:site withKey:LOCATIONSITE];
    [[NSNotificationCenter defaultCenter]postNotificationName:CHANGESITE_NOTI object:site];
    [self insertSite:site];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.followCities = [NSMutableArray arrayWithArray:[DefaultsHandler searchDataForKey:FOLLOWCITIES]];
    [self loadSiteCurrentAqi];
//    self.citiesDataDic = [DefaultsHandler sharedInstance].siteListInfo;
//    [self.siteTableView reloadData];
    NSLog(@"SiteManagerViewController viewWillAppear");
}

- (void)loadSiteCurrentAqi
{
    NSString *api = [Global api:[NSString stringWithFormat:@"Aqi/GetSiteCurrentAqi"]];
    AFHTTPRequestOperationManager *request = [Global createRequest];
    
    NSString *idString = @"";
    for (int i=0;i<self.followCities.count;i++) {
        SiteModel *site = (SiteModel *)[self.followCities objectAtIndex:i];
        if (i<self.followCities.count-1) {
            idString = [idString stringByAppendingString:[NSString stringWithFormat:@"%@,",site.Id]];
        }
        else{
            idString = [idString stringByAppendingString:site.Id];
        }
    }
    NSDictionary *parameters = [NSDictionary dictionaryWithObject:idString forKey:@"Id"];
    [request POST:api parameters:parameters success:^(AFHTTPRequestOperation * _Nonnull operation, id  _Nonnull responseObject) {
        self.dataArray = (NSArray *)responseObject;
        [self.siteTableView reloadData];
        //NSLog(@"---url:%@",operation.request.URL.absoluteString);
    } failure:^(AFHTTPRequestOperation * _Nullable operation, NSError * _Nonnull error) {
        //NSLog(@"---url:%@",operation.request.URL.absoluteString);
        [[[UIAlertView alloc] initWithTitle:@"提示" message:@"数据加载失败" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles: nil] show];
    }];
    
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [DefaultsHandler updateItems:self.followCities forKey:FOLLOWCITIES];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 100;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.followCities.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *indentifierCell = @"Cell";
    SiteTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:indentifierCell];
    if (!cell) {
        NSArray *nibs=[[NSBundle mainBundle]loadNibNamed:@"SiteTableViewCell" owner:nil options:nil];
        if (nibs.count>0) {
            for (id view in nibs) {
                if ([view isMemberOfClass:[SiteTableViewCell class]]) {
                    cell=(SiteTableViewCell *)view;
                }
            }
        }

    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    SiteModel *site = [self.followCities objectAtIndex:indexPath.row];
    if (site) {
        if ([[[DefaultsHandler searchDataForKey:LOCATIONSITE] Id]isEqualToString:site.Id]) {
            NSLog(@"---location site is:%@",site.name);
//            cell.locImageView.image = [UIImage imageNamed:@"Location"];
        }
        if([[[DefaultsHandler searchDataForKey:DEFAULTSITE] Id]isEqualToString:site.Id]){
            NSLog(@"---default site is:%@",site.name);
            cell.titleContentView.backgroundColor = [UIColor colorWithRed:1 green:1 blue:1 alpha:0.3];
        }else{
//            cell.locImageView.image = nil;
            cell.titleContentView.backgroundColor = [UIColor clearColor];
        }
        cell.citylbl.text = site.name;
        cell.backgroundColor = [UIColor clearColor];
        cell.aqilbl.text = site.aqi;
        for (NSDictionary *aqiDic in self.dataArray) {
            if ([[aqiDic objectForKey:@"SiteId"]isEqualToString:site.Id]) {
                cell.aqilbl.text = [NSString stringWithFormat:@"%@",[aqiDic objectForKey:@"Aqi"]];
            }
        }
        cell.aqilbl.backgroundColor = [DefaultsHandler getColorOrderAQIValue:cell.aqilbl.text];
    }

    cell.aqilbl.layer.cornerRadius = 4.0;
    cell.aqilbl.clipsToBounds = YES;
    cell.titleContentView.layer.cornerRadius = 10.0;
    
//    cell.detailTextLabel.text = @"18℃~20℃";
    return cell;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

-(UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row>0) {
        return UITableViewCellEditingStyleDelete;
    }
    return UITableViewCellEditingStyleNone;
}

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{//请求数据源提交的插入或删除指定行接收者。
    if (editingStyle ==UITableViewCellEditingStyleDelete) {//如果编辑样式为删除样式
        if (indexPath.row<[self.followCities count]) {
            SiteModel *site = [self.followCities objectAtIndex:indexPath.row];
            [self.followCities removeObjectAtIndex:indexPath.row];//移除数据源的数据
            [DefaultsHandler updateItems:self.followCities forKey:FOLLOWCITIES];
            [[NSNotificationCenter defaultCenter]postNotificationName:DELETESITE_NOTI object:site];
            [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationLeft];//移除tableView中的数据
        }
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //待修改
    SiteTableViewCell *cell = (SiteTableViewCell *)[tableView cellForRowAtIndexPath:indexPath];
    NSLog(@"didSelected:%@",cell.citylbl.text);
    SiteModel *site = [self.followCities objectAtIndex:indexPath.row];
    [[NSNotificationCenter defaultCenter]postNotificationName:CHANGESITE_NOTI object:site];
    AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
    [app.revealSideViewController pushOldViewControllerOnDirection:PPRevealSideDirectionLeft animated:YES];

}

#pragma --mark SiteOptionsViewControllerDelegate
-(void)insertSite:(SiteModel *)site
{
    [DefaultsHandler checkItemExistIfNotInsert:site forKey:FOLLOWCITIES];
    BOOL exist = NO;
    for (int i=0;i<self.followCities.count;i++) {
        SiteModel *model = [self.followCities objectAtIndex:i];
        if ([model.Id isEqualToString:site.Id]) {
            exist = YES;
            break;
        }
    }
    if (exist == NO) {
        [self.followCities insertObject:site atIndex:0];
        //weather insert items,update scrollerview
        [[NSNotificationCenter defaultCenter]postNotificationName:INSERTSITE_NOTI object:site];
        
        [self.siteTableView reloadData];
    }
}
#pragma -mark  scrollViewDelegate
- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
    app.revealSideViewController.panInteractionsWhenClosed = PPRevealSideInteractionNone;
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
    app.revealSideViewController.panInteractionsWhenClosed = PPRevealSideInteractionContentView;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)addBtnClick:(id)sender {
    NSLog(@"Add");
    SiteOptionsViewController *siteViewController = [[SiteOptionsViewController alloc]init];
    siteViewController.delegate = self;
    [self presentViewController:siteViewController animated:YES completion:nil];
}

- (IBAction)editBtnAction:(id)sender {
    self.siteTableView.editing = !self.siteTableView.isEditing;
    NSString *title = self.siteTableView.editing?@"取消":@"编辑";
    [self.editBtn setTitle:title forState:UIControlStateNormal];
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:LOCATION_NOTI object:nil];
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    NSLog(@"ssssssss");
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
