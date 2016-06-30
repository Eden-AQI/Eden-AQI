//
//  NSString+NullOrEmpty.h
//  PIVOT
//
//  Created by ChromSH on 15/3/12.
//  Copyright (c) 2015年 PerkinElmer, Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString(Util)

- (BOOL)isEmptyOrWhitespace;

- (BOOL)isNotEmptyAndWhitespace;

- (BOOL)isValidEmail;

- (NSString *)firstCharacter;

- (NSString *)camelStyleString;

- (NSString *)trim;

+ (NSString *)empty;
+ (NSString *)guidString;

@end
