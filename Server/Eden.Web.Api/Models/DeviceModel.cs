using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace Eden.Web.Api.Models
{
    public class DeviceModel
    {
        /// <summary>
        /// 设备唯一编号
        /// </summary>
        [Required]
        public string DeviceNumber { get; set; }

        /// <summary>
        /// 平台 (1：IOS，2：Android)
        /// </summary>
        [Required]
        public int Platform { get; set; }

        /// <summary>
        /// 设备类型(1：手机，2：平板)
        /// </summary>
        [Required]
        public int DeviceType { get; set; }

        /// <summary>
        /// 极光推送注册ID
        /// </summary>
        public string PushId { get; set; }

        public decimal? Latitude { get; set; }

        public decimal? Longitude { get; set; }
    }

    
}