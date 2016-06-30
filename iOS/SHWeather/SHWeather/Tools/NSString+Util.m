//
//  NSString+NullOrEmpty.m
//  PIVOT
//
//  Created by ChromSH on 15/3/12.
//  Copyright (c) 2015年 PerkinElmer, Inc. All rights reserved.
//

#import "NSString+Util.h"

@implementation NSString(Util)

-(BOOL)isEmptyOrWhitespace {
    NSCharacterSet* whitespace = [NSCharacterSet whitespaceAndNewlineCharacterSet];
    return [self stringByTrimmingCharactersInSet:whitespace].length == 0;
}

-(BOOL)isNotEmptyAndWhitespace {
    return ![self isEmptyOrWhitespace];
}

-(BOOL) isValidEmail
{
    if ([self isNotEmptyAndWhitespace]) {
        BOOL stricterFilter = YES;
        NSString *stricterFilterString = @"[A-Z0-9a-z\\._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}";
        NSString *laxString = @".+@([A-Za-z0-9]+\\.)+[A-Za-z]{2}[A-Za-z]*";
        NSString *emailRegex = stricterFilter ? stricterFilterString : laxString;
        NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
        
        return [emailTest evaluateWithObject:self];
    }
    
    return NO;
}

- (NSString *)firstCharacter
{
    if (self.length > 0) {
        const unichar firstChar = [self characterAtIndex:0];
        return [NSString stringWithCharacters:&firstChar length:1];
    }
    
    return nil;
}

- (NSString *)camelStyleString
{
    if (self.length>0) {
        NSString *firstCharacter = [self firstCharacter].lowercaseString;
        NSString *remaining = [self substringFromIndex:1];
        return [NSString stringWithFormat:@"%@%@",firstCharacter,remaining];
    }
    return nil;
}
- (NSString *)trim
{
    return [self stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

+ (NSString *)empty
{
    return @"";
}

+ (NSString *)guidString
{
    CFUUIDRef uuid = CFUUIDCreate(CFAllocatorGetDefault());
    
    NSString *uuidString = (__bridge NSString *)CFUUIDCreateString(CFAllocatorGetDefault(), uuid);
    
    CFRelease(uuid);
    
    return uuidString;
}

@end
