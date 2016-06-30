//
//  SiteOptionsViewController.m
//  SHWeather
//
//  Created by xiao on 6/1/16.
//  Copyright © 2016 xiao. All rights reserved.
//

//#import <BaiduMapAPI_Location/BMKLocationService.h>
#import "SiteOptionsViewController.h"

@interface SiteOptionsViewController ()<UITableViewDataSource,UITableViewDelegate,UISearchBarDelegate>

@property (weak, nonatomic) IBOutlet UISearchBar *resultSearchBar;
@property (weak, nonatomic) IBOutlet UITableView *cityTableView;

@property (nonatomic, strong) NSMutableDictionary *cities;
@property (nonatomic, strong) NSMutableArray *keys; //城市首字母
@property (nonatomic, strong) NSMutableArray *arrayCitys;   //城市数据
//@property (nonatomic, strong) NSMutableArray *arrayHotCity;

@property (nonatomic) BOOL isResultType;
@property (nonatomic, strong) NSMutableArray *tepKeys; //城市首字母
@property (nonatomic, strong) NSMutableArray *resultCities;

@property (strong, nonatomic) BMKLocationService *locService;

@end

@implementation SiteOptionsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
//    self.arrayHotCity = [NSMutableArray arrayWithObjects:@"广州市",@"北京市",@"天津市",@"西安市",@"重庆市",@"沈阳市",@"青岛市",@"济南市",@"深圳市",@"长沙市",@"无锡市", nil];
    self.keys = [NSMutableArray array];
    self.arrayCitys = [NSMutableArray array];
    
    self.cityTableView.autoresizingMask = (UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight);
    
    // Do any additional setup after loading the view from its nib.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self getCityData];
}

#pragma mark - 获取城市数据
-(void)getCityData
{
    if ([DefaultsHandler sharedInstance].siteListDic) {
        self.cities = [NSMutableDictionary dictionaryWithDictionary:[DefaultsHandler sharedInstance].siteListDic];
        [self reloadTableWithCityData];
        return;
    }
    NSString *api = [Global api:[NSString stringWithFormat:@"Aqi/GetCityList"]];
    AFHTTPRequestOperationManager *request = [Global createRequest];
    [request GET:api parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSArray *responseArray = (NSArray *)responseObject;
        NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:10];
        NSMutableDictionary *citiesNameDic = [NSMutableDictionary dictionaryWithCapacity:10];
        for (int i = 0; i<responseArray.count; i++) {
            NSDictionary *itemDic = [responseArray objectAtIndex:i];
            NSArray *cityDataArray = [itemDic objectForKey:@"Items"];
            [dic setObject:cityDataArray forKey:[itemDic objectForKey:@"GroupName"]];
            NSMutableArray *muArray = [NSMutableArray arrayWithCapacity:10];
            for (int j = 0; j<cityDataArray.count; j++) {
                NSDictionary *cityDic = [cityDataArray objectAtIndex:j];
                [muArray addObject:[cityDic objectForKey:@"Name"]];
            }
            [citiesNameDic setObject:muArray forKey:[itemDic objectForKey:@"GroupName"]];
        }
        [DefaultsHandler sharedInstance].siteListInfo = dic;
        [DefaultsHandler sharedInstance].siteListDic = citiesNameDic;
        self.cities = citiesNameDic;
        [self reloadTableWithCityData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [[[UIAlertView alloc] initWithTitle:@"提示" message:@"数据加载失败" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles: nil] show];
    }];
}

- (void)reloadTableWithCityData
{
    self.isResultType = NO;
//    NSString *path=[[NSBundle mainBundle] pathForResource:@"citydict" ofType:@"plist"];
//    self.cities = [NSMutableDictionary dictionaryWithContentsOfFile:path];
    
    [self.keys addObjectsFromArray:[[self.cities allKeys] sortedArrayUsingSelector:@selector(compare:)]];
    self.tepKeys = [NSMutableArray arrayWithArray:self.keys];
    //添加热门城市
    //NSString *strHot = @"热";
    //NSString *strLocation = @"定位";
    //[self.keys insertObject:strHot atIndex:0];
    //[self.keys insertObject:strLocation atIndex:0];
    //    [self.cities setObject:_arrayHotCity forKey:strHot];
    //[self.cities setObject:@[strLocation] forKey:strLocation];
    [self.cityTableView reloadData];
}

#pragma mark - tableView
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (self.isResultType) {
        return 0;
    }
    return 20.0;
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    if (self.isResultType) {
        return nil;
    }
    UIView *bgView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 20)];
    bgView.backgroundColor = [UIColor colorWithRed:247.0/255 green:247.0/255 blue:247.0/255 alpha:1.0];
    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(13, 0, 250, 20)];
    titleLabel.backgroundColor = [UIColor clearColor];
    titleLabel.textColor = [UIColor blackColor];
    titleLabel.font = [UIFont systemFontOfSize:12];
    
    NSString *key = [_keys objectAtIndex:section];
    if ([key rangeOfString:@"热"].location != NSNotFound) {
        titleLabel.text = @"热门城市";
    }
    else
        titleLabel.text = key;
    
    [bgView addSubview:titleLabel];
    
    return bgView;
}

- (NSArray *)sectionIndexTitlesForTableView:(UITableView *)tableView
{
    if (self.isResultType) {
        return nil;
    }
    return _keys;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    if (self.isResultType) {
        return 1;
    }
    return [_keys count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.isResultType) {
        return self.resultCities.count;
    }
    // Return the number of rows in the section.
    NSString *key = [_keys objectAtIndex:section];
    NSArray *citySection = [_cities objectForKey:key];
    return [citySection count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] ;
        cell.backgroundColor = [UIColor clearColor];
        cell.contentView.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        [cell.textLabel setTextColor:[UIColor blackColor]];
        cell.textLabel.font = [UIFont systemFontOfSize:18];
    }
    if (self.isResultType) {
        cell.imageView.image = nil;
        cell.textLabel.text = [self.resultCities objectAtIndex:indexPath.row];;
        return cell;
    }
    NSString *key = [_keys objectAtIndex:indexPath.section];
    if ([key rangeOfString:@"定位"].location != NSNotFound) {
        cell.imageView.image = [UIImage imageNamed:@"dt.png"];
        cell.textLabel.text = @"定位";
    }else{
        cell.imageView.image = nil;
        cell.textLabel.text = [[_cities objectForKey:key] objectAtIndex:indexPath.row];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication]delegate];
    if (self.isResultType) {
        UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        if (self.delegate&&[self.delegate respondsToSelector:@selector(insertSite:)]) {
            NSDictionary *siteListDic = [DefaultsHandler sharedInstance].siteListInfo;
            for (int i = 0; i<self.keys.count; i++) {
                NSArray *citiesArray = [siteListDic objectForKey:[self.keys objectAtIndex:i]];
                for (NSDictionary *itemDic in citiesArray) {
                    if ([[itemDic objectForKey:@"Name"]isEqualToString:cell.textLabel.text]) {
                        SiteModel *model = [[SiteModel alloc]initWithDic:itemDic];
                        [self.delegate insertSite:model];
                        break;
                    }
                }
            }
        }
    }else{
//        if (indexPath.section == 0) {
//            [app locationAction];
//        }else{
            UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
            if (self.delegate&&[self.delegate respondsToSelector:@selector(insertSite:)]) {
                NSString *keyStr = [self.keys objectAtIndex:indexPath.section];
                NSArray *citiesArray = [[DefaultsHandler sharedInstance].siteListInfo objectForKey:keyStr];
                for (NSDictionary *itemDic in citiesArray) {
                    if ([[itemDic objectForKey:@"Name"]isEqualToString:cell.textLabel.text]) {
                        SiteModel *model = [[SiteModel alloc]initWithDic:itemDic];
                        [self.delegate insertSite:model];
                        break;
                    }
                }
            }
//        }
    }
    [self dismissViewControllerAnimated:YES completion:nil];
    [app.revealSideViewController popViewControllerAnimated:YES];
}

#pragma --mark UISearchBarDelegate
- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    NSString *trimedSearchString = [searchBar.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if (trimedSearchString .length>0) {
        self.isResultType = YES;
        [self getResultCityDataWithSearchString:trimedSearchString];
    }else{
        self.isResultType = NO;
    }
    [self.cityTableView reloadData];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar
{
    self.isResultType = NO;
    [self.cityTableView reloadData];
}

- (void)getResultCityDataWithSearchString:(NSString *)searchText
{
    self.resultCities = [NSMutableArray arrayWithCapacity:10];
    for (int i=0;i<self.tepKeys.count;i++) {
        NSString *key = [self.tepKeys objectAtIndex:i];
        NSArray *cCities = [self.cities objectForKey:key];
        for (int j=0; j<cCities.count; j++) {
            NSString *theCity = [cCities objectAtIndex:j];
            if ([theCity containsString:searchText]) {
                [self.resultCities addObject:theCity];
            }
        }
    }
    self.resultCities = (NSMutableArray *)[self.resultCities sortedArrayUsingSelector:@selector(compare:)];
}
/*
#pragma -mark location
- (void)locationAction
{
    self.locService = [[BMKLocationService alloc]init];
    self.locService.delegate = self;
    [self.locService startUserLocationService];
}

#pragma -mark BMKLocationServiceDelegate
- (void)didUpdateUserHeading:(BMKUserLocation *)userLocation
{
    NSLog(@"didUpdateUserHeading lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
    NSLog(@"heading is %@",userLocation.heading);
}
 
- (void)didUpdateBMKUserLocation:(BMKUserLocation *)userLocation
{
    NSLog(@"didUpdateBMKUserLocation lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
    CLGeocoder *geocoder = [[CLGeocoder alloc]init];
    CLGeocodeCompletionHandler handler = ^(NSArray *place,NSError *error){
        for(CLPlacemark *placemark in place){
            NSString *cityStr = placemark.thoroughfare;
            NSString *cityName = placemark.subLocality;
            NSLog(@"----cityStr:%@-----cityName:%@",cityStr,cityName);
            
            if (self.delegate&&[self.delegate respondsToSelector:@selector(insertCity:)]) {
                [self.delegate insertCity:cityName];
            }
            [self dismissViewControllerAnimated:YES completion:nil];
            break;
        }
    };
    CLLocation *loc = [[CLLocation alloc]initWithLatitude:userLocation.location.coordinate.latitude longitude:userLocation.location.coordinate.longitude];
    [geocoder reverseGeocodeLocation:loc completionHandler:handler];
}*/

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
