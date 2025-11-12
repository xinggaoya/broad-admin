package com.broad.web.controller.job.model;

import com.broad.common.constant.ScheduleConstants;

import java.util.Arrays;
import java.util.List;

/**
 * 定时任务模块表单/筛选元数据
 */
public class SysJobMetaVO {

    private List<OptionItem> misfirePolicies;

    public List<OptionItem> getMisfirePolicies() {
        return misfirePolicies;
    }

    public void setMisfirePolicies(List<OptionItem> misfirePolicies) {
        this.misfirePolicies = misfirePolicies;
    }

    /**
     * 默认元数据
     *
     * @return 元数据
     */
    public static SysJobMetaVO defaultMeta() {
        SysJobMetaVO vo = new SysJobMetaVO();
        vo.setMisfirePolicies(buildMisfireOptions());
        return vo;
    }

    private static List<OptionItem> buildMisfireOptions() {
        return Arrays.asList(
                OptionItem.of(ScheduleConstants.MISFIRE_DEFAULT, "默认策略", "遵循触发器默认策略"),
                OptionItem.of(ScheduleConstants.MISFIRE_IGNORE_MISFIRES, "立即触发执行", "错过时间点后立即补偿执行"),
                OptionItem.of(ScheduleConstants.MISFIRE_FIRE_AND_PROCEED, "触发一次执行", "错过后补偿执行一次并恢复计划"),
                OptionItem.of(ScheduleConstants.MISFIRE_DO_NOTHING, "不触发立即执行", "只等待下次触发时间")
        );
    }

    public static class OptionItem {
        private String value;
        private String label;
        private String description;

        public static OptionItem of(String value, String label, String description) {
            OptionItem item = new OptionItem();
            item.setValue(value);
            item.setLabel(label);
            item.setDescription(description);
            return item;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
