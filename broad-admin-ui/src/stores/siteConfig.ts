import { defineStore } from 'pinia'
import { SiteConfig } from '/@/stores/interface'
import { SITE_CONFIG } from '/@/stores/constant/cacheKey'

export const useSiteConfig = defineStore('siteConfig', {
    state: (): SiteConfig => {
        return {
            site_name: '321',
            record_number: '123',
            version: '2.0.0',
        }
    },
    actions: {},
    persist: {
        key: SITE_CONFIG,
    },
})
