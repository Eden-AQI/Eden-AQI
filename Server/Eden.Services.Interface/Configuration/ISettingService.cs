using System;
using System.Collections.Generic;
using System.Linq.Expressions;
using Eden.Core.Configuration;
using Eden.Domain.Configuration;

namespace Eden.Services.Configuration
{

    public partial interface ISettingService
    {

        Setting GetSettingById(int settingId);


        void DeleteSetting(Setting setting);


        Setting GetSetting(string key);


        T GetSettingByKey<T>(string key, T defaultValue = default(T));


        void SetSetting<T>(string key, T value, bool clearCache = true);


        IList<Setting> GetAllSettings();


        bool SettingExists<T, TPropType>(T settings,
            Expression<Func<T, TPropType>> keySelector)
            where T : ISettings, new();

        T LoadSetting<T>() where T : ISettings, new();


        void SaveSetting<T>(T settings) where T : ISettings, new();


        void SaveSetting<T, TPropType>(T settings,
            Expression<Func<T, TPropType>> keySelector,
             bool clearCache = true) where T : ISettings, new();


        void DeleteSetting<T>() where T : ISettings, new();


        void DeleteSetting<T, TPropType>(T settings,
            Expression<Func<T, TPropType>> keySelector) where T : ISettings, new();


        void ClearCache();
    }
}
